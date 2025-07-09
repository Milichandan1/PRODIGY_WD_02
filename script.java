const display = document.getElementById('display');
const startBtn = document.getElementById('startBtn');
const pauseBtn = document.getElementById('pauseBtn');
const resetBtn = document.getElementById('resetBtn');
const lapBtn = document.getElementById('lapBtn');
const lapList = document.getElementById('lapList');
const lapCount = document.getElementById('lapCount');

let startTime;
let elapsedTime = 0;
let timerInterval;
let isRunning = false;
let lapTimes = [];

function formatTime(time) {
  const hours = Math.floor(time / 3600000);
  const minutes = Math.floor((time % 3600000) / 60000);
  const seconds = Math.floor((time % 60000) / 1000);
  return `${hours.toString().padStart(2, '0')}:${minutes
    .toString()
    .padStart(2, '0')}:${seconds.toString().padStart(2, '0')}`;
}

function updateDisplay() {
  display.textContent = formatTime(elapsedTime);
}

function start() {
  if (!isRunning) {
    startTime = Date.now() - elapsedTime;
    timerInterval = setInterval(() => {
      elapsedTime = Date.now() - startTime;
      updateDisplay();
    }, 10);
    isRunning = true;
    startBtn.classList.add('hidden');
    pauseBtn.classList.remove('hidden');
    resetBtn.classList.remove('hidden');
    lapBtn.classList.remove('hidden');
    display.classList.add('blink');
  }
}

function pause() {
  if (isRunning) {
    clearInterval(timerInterval);
    isRunning = false;
    pauseBtn.classList.add('hidden');
    startBtn.classList.remove('hidden');
    display.classList.remove('blink');
  }
}

function reset() {
  clearInterval(timerInterval);
  isRunning = false;
  elapsedTime = 0;
  updateDisplay();
  lapTimes = [];
  updateLapList();
  startBtn.classList.remove('hidden');
  pauseBtn.classList.add('hidden');
  resetBtn.classList.add('hidden');
  lapBtn.classList.add('hidden');
  display.classList.remove('blink');
}

function lap() {
  if (isRunning) {
    lapTimes.unshift({
      time: elapsedTime,
      display: formatTime(elapsedTime)
    });
    updateLapList();
  }
}

function updateLapList() {
  if (lapTimes.length === 0) {
    lapList.innerHTML = '<div class="text-center text-gray-500 py-4">No lap times recorded yet</div>';
    lapCount.textContent = '0 laps';
  } else {
    lapList.innerHTML = '';
    lapTimes.forEach((lap, index) => {
      const lapItem = document.createElement('div');
      lapItem.className = 'lap-item bg-gray-700 p-3 rounded-lg flex justify-between items-center';

      const lapNumber = document.createElement('span');
      lapNumber.className = 'text-gray-400 font-medium';
      lapNumber.textContent = `Lap ${lapTimes.length - index}`;

      const lapTime = document.createElement('span');
      lapTime.className = 'font-mono font-bold';
      lapTime.textContent = lap.display;

      lapItem.appendChild(lapNumber);
      lapItem.appendChild(lapTime);
      lapList.appendChild(lapItem);
    });

    lapCount.textContent = `${lapTimes.length} ${lapTimes.length === 1 ? 'lap' : 'laps'}`;
  }
}

startBtn.addEventListener('click', start);
pauseBtn.addEventListener('click', pause);
resetBtn.addEventListener('click', reset);
lapBtn.addEventListener('click', lap);

document.addEventListener('keydown', (e) => {
  if (e.code === 'Space') {
    e.preventDefault();
    isRunning ? pause() : start();
  } else if (e.code === 'KeyL') {
    if (isRunning) lap();
  } else if (e.code === 'KeyR') {
    reset();
  }
});
