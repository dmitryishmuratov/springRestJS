$(document).ready(function () {
getTable();
});

async function getTable() {
    const response = await fetch('/admin/api/all');
    const listUsers = await response.json();
    htmlTable(listUsers);
}

function htmlTable(listUsers) {
    let htmlTable = "";
    for (let data of listUsers) {
        htmlTable += `<tr id="list">
                            <td id="tableId">${data.id}</td>
                            <td id="tableUsername">${data.username}</td>
                            <td id="tableEmail">${data.email}</td>
                            <td id="tablePassword" hidden>${data.password}</td>
                            <td id="tableRole">${data.roles[0].role}</td>
                            <td><button id = "editUserBtn"  type = "button"
                                 class = "btn btn-primary" data-toggle = "modal"
                                 data-target = "#editUser"
                                 data-id = "${listUsers.id}" >Update</button></td>
                            </tr>`;
    }
    $("#tableUser #list").remove();
    $("#tableUser").append(htmlTable);
}

// addUser

$("#addFormUser").on("click", function (event) {
    event.preventDefault();
    addUser();
    $(":input", "#addForm").val("");
});

async function addUser() {
    let url = "/admin/api/add";
    let user = {
        'username': $("#addUsername").val(),
        'email': $("#addEmail").val(),
        'password': $("#addPassword").val(),
        'role': $("#addRole").val()
    };
    let dateUser = {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(user)
    };
    const response = await fetch(url, dateUser);
    const data = await response.json();
    await getTable();
}

// update

$(document).on("click", "#editUserBtn", function () {

    let id = $(this).closest("tr").find("#tableId").text();

    async function getUser(id) {
        let resp = await fetch('/admin/api/get/' + id);
        let data = await resp.json();
        return data;
    }

    (async function() {
        let user = await getUser(id);
        $('#updateId').val(user.id);
        $('#updateUsername').val(user.username);
        $('#updateEmail').val(user.email);
        $('#updatePassword').hidden.val(user.password);

        let role = $('#updateRole').val(user.role);
        let admin = "admin";
        if (role === admin) {
            $('#updateRole option:contains("admin")').prop("selected", true);
        } else {
            $('#updateRole option:contains("user")').prop("selected", true);
        }
        getTable();
    })();
});

$("#updateFormUser").on("click", function (event) {
    event.preventDefault();
    updateForm();
    $("#editUser").modal("toggle");
});

async function updateForm() {
    let user = {
        'id': $("#updateId").val(),
        'username': $("#updateUsername").val(),
        'email': $("#updateEmail").val(),
        'password': $("#updatePassword").val(),
        'role': $("#updateRole").val()
    };

    let option = {
        headers: {"Content-Type": "application/json"},
        method: 'PUT',
        body: JSON.stringify(user),
    };

    let url = "/admin/api/edit";
    const response = await fetch(url, option);
    const data = await response.json();
    await getTable();
}

// delete

$(document).on('click', '#deleteUser', function (del) {
    del.preventDefault();
    deleteUser().then(r => r.toLocaleString());
});

async function deleteUser() {
    let id = $("#updateId").val();
    const response = await fetch("/admin/api/delete/" + id, {
        method: 'DELETE',
        headers: {
            "Content-Type": 'application/json'
        }
    });
    const data = await response.json();
    await getTable();
}