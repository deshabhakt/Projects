var counter=0;

function predictor(){
    counter++;
    console.log(counter);
    var d1_img = document.querySelector("#player-1-img")
    var d2_img = document.querySelector("#player-2-img")
    var result = document.querySelector("#result")
    var d1_val = Math.floor(Math.random()*6+1);
    var d2_val = Math.floor(Math.random()*6+1);
    d1_img.setAttribute("src",getImageLocation(d1_val))
    d2_img.setAttribute("src",getImageLocation(d2_val))
    if(d1_val==d2_val){
        result.innerHTML = "Wooo!! It's a Tie"
    }
    else if(d1_val>d2_val){
        result.innerHTML = "🚩Player-1 Wins!"
    }
    else{
        result.innerHTML = "Player-2 Wins!🚩"
    }
}

function getImageLocation(key) {
    flag = true;
    switch (key) {
        case 1:
            return "image/dice-1.png"
        case 2:
            return "image/dice-2.png"
        case 3:
            return "image/dice-3.png"
        case 4:
            return "image/dice-4.png"
        case 5:
            return "image/dice-5.png"
        
        default:
            return "image/dice-6.png"
    }
}