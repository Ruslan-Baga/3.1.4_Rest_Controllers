const action = {
    update: 1, delete: 2
}

$(document).ready(function () {
    adminInfo();
    getAllUsers();
});


async function adminInfo() {
    const response = await fetch('/api/user', {
        method: 'GET',
        headers: {'Content-Type': 'application/json'}
    });

    const {id, password, firstName, lastName, age, email, rolesAsString} = await response.json();

    $('#currentUser').text(email);
    $('#roles').text(rolesAsString);
}

async function getAllUsers() {
    const response = await fetch('/api/admin', {
        method: 'GET',
        headers: {'Content-Type': 'application/json'}
    });

    const users = await response.json();
    const tbody = $('#usersTableBody');

    tbody.empty();
    users.forEach((user) => {
        const {id, firstName, lastName, age, email, rolesAsString} = user;

        tbody.append(`<tr>
        <td>${id}</td>
        <td>${firstName}</td>
        <td>${lastName}</td>
        <td>${age}</td>
        <td>${email}</td>
        <td>${rolesAsString}</td>
        <td><button type="button" class="btn btn-primary" id="editButton"  onclick="openModalHandler(${action.update}, parseInt(${id}))">Edit</button></td>
        <td><button type="button" class="btn btn-danger" id="deleteButton" onclick="openModalHandler(${action.delete}, ${id})">Delete</button></td>
        </tr>`)

    });
}

document.getElementById('addUserForm').addEventListener('submit', async e => {
    e.preventDefault();
    $('.validation-error').remove();

    const response = await saveUser();
    if (!response.ok) {
        const errors = await response.json();
        displayValidationErrors(errors);
    } else {
        $('#addUserForm')[0].reset();
        getAllUsers();
        $('#adminUsersTable').click();
    }
});

async function saveUser() {

    let addRoles = [];
    let options = document.getElementById('role').options;
    for (let opt of options) {
        if (opt.selected) {

            const roleObject = {
                id: parseInt(opt.getAttribute('data-id')),
                role: opt.value,
                authority: opt.value
            };
            addRoles.push(roleObject);
        }
    }

    console.log(addRoles);

    const newUser = {
        password: $('#password').val(),
        firstName: $('#firstName').val(),
        lastName: $('#lastName').val(),
        age: parseInt($('#age').val()),
        email: $('#email').val(),
        roleUser: addRoles
    };

    console.log(newUser);

    const response = await fetch('api/admin', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(newUser)
    });
    return response;
}
function displayValidationErrors(errors) {
    $('.validation-error').remove();

    errors.forEach(error => {
        const field = error.field;
        const message = error.defaultMessage;

        $(`[name="${field}"]`).after(`<div class="validation-error text-danger">${message}</div>`);
    });
}


async function openModalHandler(actionId, userId = null) {

    const modal = $('#editUserModal');

    $('.validation-error').remove();

    modal.modal();

    const modalFields = modal.find('.mb-3');
    const idField = modalFields.children('#editUserId').first();
    const firstNameField = modalFields.children('#editFirstName').first();
    const lastNameField = modalFields.children('#editLastName').first();
    const ageField = modalFields.children('#editAge').first();
    const emailField = modalFields.children('#editEmail').first();
    const passwordField = modalFields.children('#editPassword').first();
    const rolesField = modalFields.children('#editRoles').first();
    const submitBtn = $('#submitEdit');
    const modalLabel = $('#editUserModalLabel');
    const closeBtn = $('#closeModal')

    closeBtn.on('click', function () {
        location.reload();
    });

    const {id, firstName, lastName, age, email, password} = await getUser(userId);

    idField.val(id);
    idField.parent().prop('hidden', false);
    firstNameField.val(firstName).prop('disabled', false);
    lastNameField.val(lastName).prop('disabled', false);
    ageField.val(age).prop('disabled', false);
    emailField.val(email).prop('disabled', false);
    passwordField.val(password).prop('disabled', false);

    submitBtn.off('click').on('click', async (e) => {
        e.preventDefault();
        await submitBtnHandler(editUser);
    });

        switch (actionId) {
        case action.delete:
            modalLabel.text('Delete user');
            submitBtn
                .removeClass()
                .addClass('btn btn-danger')
                .text('Delete')
                .off('click')
                .click(() => submitBtnHandler(deleteUser));
            firstNameField.prop('disabled', true);
            lastNameField.prop('disabled', true);
            ageField.prop('disabled', true);
            emailField.prop('disabled', true);
            passwordField.prop('disabled', true);
            rolesField.prop('disabled', true);
            break;
        case action.update:
            modalLabel.text('Edit user');
            submitBtn
                .removeClass()
                .addClass('btn btn-primary')
                .text('Save changes')
            break;
    }
}
async function getUser(userId) {
    const response = await fetch(`/api/admin/${userId}`, {
        method: 'GET', headers: {'Content-Type': 'application/json'}
    });

    return response.json();
}

function collectUserData() {
    const modalFields = $('#editUserModal').find('.mb-3');

    const idField = modalFields.children('#editUserId').first();
    const firstNameField = modalFields.children('#editFirstName').first();
    const lastNameField = modalFields.children('#editLastName').first();
    const ageField = modalFields.children('#editAge').first();
    const emailField = modalFields.children('#editEmail').first();
    const passwordField = modalFields.children('#editPassword').first();


    const user = {
        roleUser: []
    };
    if (idField.val()) {
        user.id = parseInt(idField.val());
    }

    if (firstNameField.val()) {
        user.firstName = firstNameField.val();
    }

    if (lastNameField.val()) {
        user.lastName = lastNameField.val();
    }
    if (ageField.val()) {
        user.age = parseInt(ageField.val());
    }

    if (emailField.val()) {
        user.email = emailField.val();
    }


    if (passwordField.val()) {
        user.password = passwordField.val();
    }

    let addRoles = [];
    let options = document.getElementById('editRoles').options;
    for (let opt of options) {
        if (opt.selected) {

            const roleObject = {
                id: parseInt(opt.getAttribute('data-id')),
                role: opt.value,
                authority: opt.value
            };
            addRoles.push(roleObject);
        }
    }
    user.roleUser = addRoles;
    console.log("Collect " + user);
    return user;
}

async function submitBtnHandler(callback) {
    const modal = $('#editUserModal');
    const user = collectUserData();

    try {
        const response = await callback(user);
        if (!response.ok) {
            const errors = await response.json();
            displayValidationErrors(errors);
            modal.modal('show');
        } else {
            await getAllUsers();
            modal.modal('hide');
        }
    } catch (error) {
        console.error("Error during submission:", error);
    }
}
$(document).ready(function () {
    $('#closeModalButton').on('click', function () {
        location.reload();
    });
});

const deleteUser = async ({id}) => fetch(`/api/admin/${id}`, {
    method: 'DELETE', headers: {'Content-Type': 'application/json'}
});

const editUser = async (user) => fetch(`/api/admin`, {
    method: 'PUT',
    headers: {'Content-Type': 'application/json'},
    body: JSON.stringify(user)
});