const api_url = 'http://localhost:8080/api';
const fetchWrapper = async (path, data) => {
    const response = await fetch(api_url + path, data);
    switch (response.status) {
        case 401:
            window.location.assign('/login')
            break;
    }
    return response;
}
const populateRow = (value) => {
    return '<tr>\n' +
        '            <td>' + value.id + '</td>\n' +
        '            <td>' + value.name + '</td>\n' +
        '            <td>' + value.surname + '</td>\n' +
        '            <td>' + value.age + '</td>\n' +
        '            <td>' + value.username + '</td>\n' +
        '            <td>' + value.roles.reduce((sum, current) => sum + ' ' + current.name, '') + '</td>\n' +
        /*'            <td><button class="btn btn-info text-white btn-edit" onclick="fetchEdit(' + value.id + ')">Edit</button></td>\n' +
        '            <td><button class="btn btn-danger btn-delete" disabled onclick="fetchDelete(' + value.id + ')">Delete</button></td>\n' +*/
        '          </tr>'
}
const fetchCurrentUser = async () => {
    const response = await fetchWrapper('/');
    if (!response.ok) {
        console.log("Ошибка выполнения запроса к серверу: невозможно загрузить список пользователей");
        return;
    }
    const json = await response.json();
    document.getElementById('user-detail').innerHTML = json.username + ' with roles: '
        + json.roles.map(value => value.name).reduce((sum, current) => sum + ' ' + current)
    document.getElementById("current-user-table-body").innerHTML = populateRow(json)
}

const logoutAction = async (ev) => {
    fetchWrapper('/logout').then(() => window.location.assign('/'))
    ev.preventDefault()
}

const init = async () => {
    await fetchCurrentUser()
    document.getElementById('logout').onclick = logoutAction
}

window.onload = init
