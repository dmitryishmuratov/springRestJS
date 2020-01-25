$(function () {
    getTable();
});

function getTable() {
    fetch('/admin/api/all')
        .then ( function (response) {
                if (response.status !== 200) {
                   // console.log(response.status);
                    return;
                }
                response.json().then(function (listUsers) {
                    let htmlTable = "";
                    for (let i = 0; i < listUsers.length; i++) {
                        let htmlRole = listUsers[i].roles[0].role;

                        htmlTable += '<tr id="list">' +
                            '<td id="tableId" class="text-center">' + listUsers[i].id + '</td>' +
                            '<td id="tableUsername">' + listUsers[i].username + '</td>' +
                            '<td id="tableEmail">' + listUsers[i].email + '</td> ' +
                            '<td id="tablePassword" hidden>' + listUsers[i].password + '</td>' +
                            '<td id="tableRole">' + htmlRole + '</td>' +
                            '<td><button id="editUserBtn" type="button" class="btn btn-primary" ' +
                            'data-toggle="modal" data-target="#editUser" data-id="${listUsers.id}">Update</button></td>' +
                            '</tr>';
                    }
                    $("#tableUser #list").remove();
                    $("#tableUser").append(htmlTable);
                });
            }
        )
        .catch(function (err) {
            console.log('Fetch Error :-S', err);
        });
}

// addUser

$("#addFormUser").on("click", function (event) {
    event.preventDefault();
    addForm();
    $(":input", "#addForm").val("");
});

// $("#resetTable").on("click", function () {
//     getTable();
// });

async function addForm() {
    let user = {
        'username': $("#addUsername").val(),
        'email': $("#addEmail").val(),
        'password': $("#addPassword").val(),
        'role': $("#addRole").val()
    };
    let url = "/admin/api/add";
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
    getTable();
}

// update

$(document).on("click", "#editUserBtn", function () {

    let id = $(this).closest("tr").find("#tableId").text();


    async function getUser(id) {
        let resp = await fetch('/admin/api/get/' + id);
        let data = await resp.json();
        return data;
    }

    (async function main() {
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
        console.log(user);
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
    console.log(user);
    let option = {
        headers: {
            "Content-Type": "application/json"
        },
        method: 'PUT',
        body: JSON.stringify(user),
    };
    let url = "/admin/api/edit";
    const response = await fetch(url, option);
    const data = await response.json();
    getTable();
    console.log(data);
}

// delete

// $('#deleteUser').click(function (del) {
//     del.preventDefault();
//     deleteUser();
// });

$(document).on('click', '#deleteUser',function (del) {
    del.preventDefault();
    deleteUser();
});

async function deleteUser() {
    let id = $("#updateId").val();
    const response = await fetch("/admin/api/delete/" + id, {
        method: 'DELETE',
        headers: {
            "Content-Type" : 'application/json'
        }
    });
    const data = await response.json();
    getTable();
    console.log(data);
}