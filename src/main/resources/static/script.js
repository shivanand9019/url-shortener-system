function shorten(){

    const url = document.getElementById("urlInput").value;


    fetch("/api/shorten",{
        method:"POST",
        headers:{
            "Content-Type":"application/json"
        },
        body:JSON.stringify({url:url})
    })
        .then(res => res.text())
        .then(data=>{
            document.getElementById("result").innerHTML =
                "<a href='" + data + "' target='_blank'>" +data + "</a>";
        });

}

