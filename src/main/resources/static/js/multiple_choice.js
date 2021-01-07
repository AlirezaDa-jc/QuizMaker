var option = 4;
function removeCustom(id) {
    if(option > 0) {
        console.log(id);
        document.getElementById(id.name).hidden = true;
        document.getElementsByName(id.name)[0].hidden = true;
        document.getElementsByName(id.name)[1].hidden = true;
        option = option-1;
    }


}