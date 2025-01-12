$(document).ready(function () {
    return userInfo();
});

async function userInfo() {
    const response = await fetch('/api/user', {
        method: 'GET',
        headers: {'Content-Type': 'application/json'}
    });

    const {id, username, password, firstName, lastName, age, email, rolesAsString} = await response.json();

    $('#currentUser').text(email);
    $('#roles').text(rolesAsString);


    $('#userTableBody').append(`<tr>
        <td>${id}</td>
        <td>${firstName}</td>
        <td>${lastName}</td>
        <td>${age}</td>
        <td>${email}</td>
        <td>${rolesAsString}</td>
      </tr>`);

    let isAdmin = rolesAsString.some(role => role.name === 'ROLE_ADMIN');
    if (isAdmin) {
        $('#adminLink').show();
    }
}
