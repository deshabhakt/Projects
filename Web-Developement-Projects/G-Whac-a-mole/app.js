const squares = document.querySelectorAll('.square')
const mole = document.querySelector('.mole')
const timeLeft = document.querySelector('#time-left')
const score = document.querySelector('#score')
const output = document.querySelector('#output')
const speed_html = document.querySelector('#speed')
let result = 0
let hitPosition
let currentTime = 12
timeLeft.textContent = currentTime
let timerId = null

let speed = 500

function moveMole(speed){
    if(currentTime>0){
    timerId = setInterval(randomSquare, speed)
    speed_html.textContent = ((1/speed)*10).toFixed(3)
    }
}

function randomSquare(){
    squares.forEach(square =>{
        square.classList.remove('mole')
    })

    let randomSquare = squares[Math.floor(Math.random()*9)]
    randomSquare.classList.add('mole')

    hitPosition = randomSquare.id
}


squares.forEach(square =>{
    square.addEventListener('mousedown',()=>{
        if(square.id  == hitPosition){
            result++
            speed = speed*0.8
            clearInterval(timerId)
            moveMole(speed)
            score.textContent = result;
            hitPosition = null
        }
        else if(square.id != hitPosition)
        {
            if (result>0) {
                result--;
            }
            speed = speed*1.2
            clearInterval(timerId)
            moveMole(speed)
            score.textContent = result
            hitPosition = null
        }
    })
})
moveMole(speed)
function countDown(){
    currentTime--
    timeLeft.textContent = currentTime
    if(currentTime<10){
        timeLeft.style.color = 'red'
    }
    if(currentTime == 0){
        if(result==0){
            output.style.color='red'
            output.textContent = 'Game OVER! Your final score is ' + result
        }
        else{
            output.style.color= 'green'
            output.textContent = 'Game OVER! Your final score is ' + result
        }
        clearInterval(timerId)
        clearInterval(countDownTimerId)
    }
}

let countDownTimerId = setInterval(countDown, 1000)

