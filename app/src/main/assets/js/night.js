var body = document.body;
alert(1);

function set_night_mode(mode) {
    body.className += mode ? 'night ' : ' ';
    return 6;
}

set_night_mode(true);
