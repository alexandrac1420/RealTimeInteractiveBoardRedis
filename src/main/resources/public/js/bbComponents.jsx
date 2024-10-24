function BBCanvas() {
    const [svrStatus, setSvrStatus] = React.useState({
        loadingState: 'Loading Canvas...'
    });
    const comunicationWS = React.useRef(null);
    const myp5 = React.useRef(null);
    const sketch = function (p) {
        p.setup = function () {
            p.createCanvas(700, 410);
        }
        p.draw = function () {
            if (p.mouseIsPressed === true) {
                p.fill(0, 0, 0);
                p.ellipse(p.mouseX, p.mouseY, 20, 20);
                comunicationWS.current.send(p.mouseX, p.mouseY);
            }
            if (p.mouseIsPressed === false) {
                p.fill(255, 255, 255);
            }
        }
    };
    React.useEffect(() => {
        myp5.current = new p5(sketch, 'container');
        setSvrStatus({ loadingState: 'Canvas Loaded' });
        comunicationWS.current = new WSBBChannel(BBServiceURL(),
            (msg) => {
                let obj = JSON.parse(msg);
                console.log("On func call back ", msg);
                drawPoint(obj.x, obj.y);
            });
        return () => {
            console.log('Clossing connection ...')
            comunicationWS.current.close();
        };
    }, []);
    function drawPoint(x, y) {
        myp5.current.ellipse(x, y, 20, 20);
    }
    return (
        <div>
            <h4>Drawing status: {svrStatus.loadingState}</h4>
        </div>);
}


function Editor({ name }
) {
    return (
        <div>
            <h1>Hello, {name}</h1>
            <hr />
            <div id="toolstatus"></div>
            <hr />
            <div id="container">
                <BBCanvas />
            </div>
            <hr />
            <div id="info"></div>
        </div>
    );
}
// Definir los tipos de propiedades esperadas
Editor.propTypes = {
    name: PropTypes.string.isRequired,  // Asegurarse de que 'name' sea una cadena y es requerida
};

const root = ReactDOM.createRoot(document.getElementById("root"));
root.render(
    <Editor name="Alexandra" />
);


// Retorna la url del servicio. Es una función de configuración.
function BBServiceURL() {
    let url = WShostURL() + '/bbService';
    console.log("BBService URL Calculada: " + url);
    return url;
}
// Retorna la url del servicio. Es una función de configuración.
function ticketServiceURL() {
    let url = RESThostURL() + '/getticket';
    console.log("ticketService URL Calculada: " + url);
    return url;
}
// Retorna la url del servicio. Es una función de configuración.
function WShostURL() {
    let host = window.location.host;
    let url = 'wss://' + (host);
    console.log("host URL Calculada: " + url);
    return url;
}
// Retorna la url del servicio. Es una función de configuración.
function RESThostURL() {
    let host = window.location.host;
    let protocol = window.location.protocol;
    let url = protocol + '//' + (host);
    console.log("host URL Calculada: " + url);
    return url;
}
// Retorna la url del servicio. Es una función de configuración.
async function getTicket() {
    const response = await fetch(ticketServiceURL());
    console.log("ticket: " + response);
    return response;
}

class WSBBChannel {
    constructor(URL, callback) {
        this.URL = URL;
        this.wsocket = new WebSocket(URL);
        this.wsocket.onopen = (evt) => this.onOpen(evt);
        this.wsocket.onmessage = (evt) => this.onMessage(evt);
        this.wsocket.onerror = (evt) => this.onError(evt);
        this.receivef = callback;
    }
    async onOpen(evt) {
        console.log("In onOpen", evt);
        let response = await getTicket();
        let json;
        if (response.ok) {
            // // if HTTP-status is 200-299
            // get the response body (the method explained below)
            json = await response.json();
        } else {
            console.log("HTTP-Error: " + response.status);
        }
        this.wsocket.send(json.ticket);
    }
    onMessage(evt) {
        console.log("In onMessage", evt);
        // Este if permite que el primer mensaje del servidor no se tenga en cuenta.
        // El primer mensaje solo confirma que se estableció la conexión.
        // De ahí en adelante intercambiaremos solo puntos(x,y) con el servidor
        if (evt.data != "Connection established.") {
            this.receivef(evt.data);
        }
    }
    onError(evt) {
        console.error("In onError", evt);
    }
    send(x, y) {
        let msg = '{ "x": ' + (x) + ', "y": ' + (y) + "}";
        console.log("sending: ", msg);
        this.wsocket.send(msg);

    }
}
