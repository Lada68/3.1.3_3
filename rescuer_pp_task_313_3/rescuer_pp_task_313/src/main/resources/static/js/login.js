const api_url = 'http://localhost:8080/api';
const fetchWrapper = async (path, data) => {
    const response = await fetch(api_url + path, data);
    switch (response.status) {
        case 401:
            window.location.assign('/')
            break;
    }
    return response;
}

const loginAction = async (ev) => {
    if (document.getElementById('sign-in-form').reportValidity()) {
        fetchWrapper('/login', {
            method: 'POST',
            body: new FormData(document.getElementById('sign-in-form'))
        }).then(value => {
            if (value.ok) window.location.assign('/')
        })
    }
    ev.preventDefault()
}


const init = async () => {
    document.getElementById('sign-in-modal-submit').onclick = loginAction
}

window.onload = init
