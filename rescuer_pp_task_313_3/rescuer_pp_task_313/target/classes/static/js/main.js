const openUsers = () => {
    document.getElementById('users').classList.remove('hidden')
    document.getElementById('newuser').classList.add('hidden')
    document.getElementById('users-btn').classList.add('active')
    document.getElementById('newuser-btn').classList.remove('active')
}
const openNewUser = () => {
    document.getElementById('users').classList.add('hidden')
    document.getElementById('newuser').classList.remove('hidden')
    document.getElementById('users-btn').classList.remove('active')
    document.getElementById('newuser-btn').classList.add('active')
    fetchRolesSelect(document.getElementById('new-role-select'), ['ROLE_USER'])
        .then(() => {
        })
}
const toggleAdminCards = () => {
    if (!document.getElementById('users-btn')) return
    if (!document.getElementById('newuser-btn')) return
    document.getElementById('users-btn').addEventListener('click', openNewUser)
    document.getElementById('newuser-btn').addEventListener('click', openNewUser)
}
const togglePages = () => {
    if (!document.getElementById('adminpage-btn')) return
    if (!document.getElementById('userpage-btn')) return
    document.getElementById('adminpage-btn').addEventListener('click', () => {
        document.getElementById('userpage').classList.add('hidden')
        document.getElementById('adminpage').classList.remove('hidden')
        document.getElementById('adminpage-btn').classList.add('active')
        document.getElementById('userpage-btn').classList.remove('active')
    })
    document.getElementById('userpage-btn').addEventListener('click', () => {
        document.getElementById('userpage').classList.remove('hidden')
        document.getElementById('adminpage').classList.add('hidden')
        document.getElementById('adminpage-btn').classList.remove('active')
        document.getElementById('userpage-btn').classList.add('active')
    })
}

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
const populateTable = (beanArray) => {
    return beanArray.map((value, index, array) => {
        return '<tr>\n' +
            '            <td>' + value.id + '</td>\n' +
            '            <td>' + value.name + '</td>\n' +
            '            <td>' + value.surname + '</td>\n' +
            '            <td>' + value.age + '</td>\n' +
            '            <td>' + value.username + '</td>\n' +
            '            <td>' + value.roles.reduce((sum, current) => sum + ' ' + current.name, '') + '</td>\n' +
            '            <td><button class="btn btn-info text-white btn-edit" onclick="fetchEdit(' + value.id + ')">Edit</button></td>\n' +
            '            <td><button class="btn btn-danger btn-delete" onclick="fetchDelete(' + value.id + ')">Delete</button></td>\n' +
            '          </tr>'
    }).reduce((sum, current) => sum + '\n' + current, '')
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
const fetchAllUsers = async () => {
    const response = await fetchWrapper('/users');
    if (!response.ok) {
        console.log("Ошибка выполнения запроса к серверу: невозможно загрузить список пользователей");
        return;
    }
    const json = await response.json();
    document.getElementById("all-users-table-body").innerHTML = populateTable(json)

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

const populateSelect = (json, selectedRolesNames) => {
    return json.map(value => '<option value="' + value.id + '"' +
        (selectedRolesNames.includes(value.name) ? ' selected ' : ' ') + '>'
        + value.name + '</option>').reduce((sum, current) => sum + '\n' + current)
}
const fetchRolesSelect = async (optionNode, selectedRolesNames) => {
    const roles = await fetchWrapper('/roles')
    const json = await roles.json()
    optionNode.innerHTML = populateSelect(json, selectedRolesNames)
}

const showEdit = () => {
    document.getElementById('edit-modal').style.display = 'block'
    document.getElementById('edit-modal').classList.add('show')
}
const hideEdit = () => {
    document.getElementById('edit-modal').classList.remove('show')
    document.getElementById('edit-modal').style.display = 'none'
}
const editAction = async (data) => {
    await fetchWrapper('/users', {
        method: 'PUT',
        body: JSON.stringify(data),
        headers: {
            'Content-Type': 'application/json'
        }
    });
}
const editCloseEvent = (ev) => {
    hideEdit()
    ev.preventDefault()
}
const editSubmitEvent = (ev) => {
    const data = {
        id: document.getElementById('edit-id').value,
        name: document.getElementById('edit-first-name').value,
        surname: document.getElementById('edit-last-name').value,
        username: document.getElementById('edit-email').value,
        password: document.getElementById('edit-password').value === '*CRYPT*' ?
            null : document.getElementById('edit-password').value,
        age: document.getElementById('edit-age').value,
        roles: [{
            id: document.getElementById('edit-role-select')
                [document.getElementById('edit-role-select').selectedIndex].value,
            name: ''
        }]
    };
    if (document.getElementById('edit-form').reportValidity()) {
        editAction(data)
            .then(() => fetchAllUsers().then(() => hideEdit()))
    }
    ev.preventDefault()
}
const populateEdit = (json) => {
    fetchRolesSelect(document.getElementById('edit-role-select'),
        json.roles.map(value => value.name))
        .then(() => {
            document.getElementById('edit-id').value = json.id
            document.getElementById('edit-first-name').value = json.name
            document.getElementById('edit-last-name').value = json.surname
            document.getElementById('edit-age').value = json.age
            document.getElementById('edit-email').value = json.username
            document.getElementById('edit-password').value = '*CRYPT*'
        })
}
const registerEditButtons = () => {
    document.getElementById('edit-modal-close').onclick = (editCloseEvent)
    document.getElementById('edit-modal-submit').onclick = (editSubmitEvent)
}
const fetchEdit = async (id) => {
    const response = await fetchWrapper('/users/' + id);
    if (!response.ok) {
        console.log("Ошибка выполнения запроса к серверу: невозможно загрузить список пользователей");
        return;
    }
    const json = await response.json();
    populateEdit(json);
    registerEditButtons();
    showEdit()
}

const showDelete = () => {
    document.getElementById('delete-modal').style.display = 'block'
    document.getElementById('delete-modal').classList.add('show')
}
const hideDelete = () => {
    document.getElementById('delete-modal').classList.remove('show')
    document.getElementById('delete-modal').style.display = 'none'
}
const deleteAction = async (id) => {
    await fetchWrapper('/users/' + id, {method: 'DELETE'})
}
const deleteCloseEvent = (ev) => {

    hideDelete()
    ev.preventDefault()
}
const deleteSubmitEvent = (ev) => {
    deleteAction(document.getElementById('delete-id').value)
        .then(() => fetchAllUsers().then(() => hideDelete()))
    ev.preventDefault()
}
const populateDelete = (json) => {
    fetchRolesSelect(document.getElementById('delete-role-select'),
        json.roles.map(value => value.name))
        .then(() => {
            document.getElementById('delete-id').value = json.id
            document.getElementById('delete-first-name').value = json.name
            document.getElementById('delete-last-name').value = json.surname
            document.getElementById('delete-age').value = json.age
            document.getElementById('delete-email').value = json.username
        })
}
const registerDeleteButtons = () => {
    document.getElementById('delete-modal-close').onclick = deleteCloseEvent
    document.getElementById('delete-modal-submit').onclick = deleteSubmitEvent
}
const fetchDelete = async (id) => {
    const response = await fetchWrapper('/users/' + id);
    if (!response.ok) {
        console.log("Ошибка выполнения запроса к серверу: невозможно загрузить список пользователей");
        return;
    }
    const json = await response.json();
    populateDelete(json);
    registerDeleteButtons();
    showDelete();
}

const fetchNew = async (ev) => {
    if (document.getElementById('new-form').reportValidity()) {
        const data = {
            id: null,
            name: document.getElementById('new-first-name').value,
            surname: document.getElementById('new-last-name').value,
            age: document.getElementById('new-age').value,
            username: document.getElementById('new-email').value,
            password: document.getElementById('new-password').value,
            roles: [{
                id: document.getElementById('new-role-select')
                    [document.getElementById('new-role-select').selectedIndex].value,
                name: null
            }]
        }
        fetchWrapper('/users', {
            method: 'POST',
            body: JSON.stringify(data),
            headers: {
                'Content-Type': 'application/json'
            }
        })
            .then(() => fetchAllUsers()
                .then(() => openUsers()))
    }
    ev.preventDefault()
}

const logoutAction = async (ev) => {
    fetchWrapper('/logout').then(() => window.location.assign('/'))
    ev.preventDefault()
}

const init = async () => {
    toggleAdminCards()
    togglePages()
    await fetchAllUsers()
    await fetchCurrentUser()
    await fetchRolesSelect(document.getElementById('new-role-select'), ['ROLE_ADMIN'])
    document.getElementById('new-form-submit').onclick = fetchNew
    document.getElementById('logout').onclick = logoutAction
}

window.onload = init
