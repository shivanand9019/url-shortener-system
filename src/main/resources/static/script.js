function shorten(){

    const url = document.getElementById("urlInput").value;
    const customCode= document.getElementById("customCode").value;
    const expirationTime= document.getElementById("expirationTime").value;


    fetch("/api/shorten",{
        method:"POST",
        headers:{
            "Content-Type":"application/json"
        },
        body:JSON.stringify({
            url: url,
            customCode:customCode,
            expirationTime:expirationTime


        })
    })
        .then(res => res.text())
        .then(data=>{
            document.getElementById("result").innerHTML =
                "<a href='" + data + "' target='_blank'>" +data + "</a>";
        });

}

async function getAnalytics(){

    const shortCode = document.getElementById("shortCode").value;
    const response = await fetch("/analytics/"+ shortCode);

    const data = await response.json();


    document.getElementById("originalUrl").innerText = data.originalUrl;
    document.getElementById("shortUrl").innerText = data.shortCode;
    document.getElementById("clickCount").innerText = data.clickCount;
    document.getElementById("createdAt").innerText = data.createdAt;
    document.getElementById("expirationTime").innerText = data.expirationTime;

}