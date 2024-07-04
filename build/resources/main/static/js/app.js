async function fetchOrders() {
    try {
        const completedResponse = await fetch('/api/cafe/completed-orders');
        const pendingResponse = await fetch('/api/cafe/orders');

        if (completedResponse.ok && pendingResponse.ok) {
            const completedOrders = await completedResponse.json();
            const pendingOrders = await pendingResponse.json();

            updateCompletedOrders(completedOrders);
            updatePendingOrders(pendingOrders);
        } else {
            console.error('Failed to fetch orders');
        }
    } catch (error) {
        console.error('Error:', error);
    }
}

function updateCompletedOrders(orders) {
    for (let i = 0; i < orders.length && i < 5; i++) {
        const orderBox = document.getElementById(`completedOrder${i + 1}`);
        orderBox.querySelector('.order-id').textContent = orders[i].orderId;
        orderBox.querySelector('.order-name').textContent = orders[i].coffeeName;
    }

    for (let i = orders.length; i < 5; i++) {
        const orderBox = document.getElementById(`completedOrder${i + 1}`);
        orderBox.querySelector('.order-id').textContent = '-';
        orderBox.querySelector('.order-name').textContent = '';
    }
}

function updatePendingOrders(orders) {
    for (let i = 0; i < 5; i++) {
        const orderBox = document.getElementById(`pendingOrder${i+1}`);
        if (orders[i]) {
            orderBox.querySelector('.order-id').textContent = orders[i].orderId;
            orderBox.querySelector('.order-name').textContent = orders[i].coffeeName;
        } else {
            orderBox.querySelector('.order-id').textContent = '-';
            orderBox.querySelector('.order-name').textContent = '';
        }
    }
}

function updateTime() {
    const now = new Date();
    const dayOfWeek = ['일', '월', '화', '수', '목', '금', '토'];
    const day = dayOfWeek[now.getDay()];
    const dateInfo = document.getElementById('datetime-info');
    const formattedDateTime = `${now.toLocaleDateString('ko-KR')}(${day}) ${now.toLocaleTimeString('ko-KR')}`;
    const formattedDateTimeWithoutSeconds = formattedDateTime.slice(0, -3);
    dateInfo.textContent = formattedDateTimeWithoutSeconds;
}

async function updateWeather() {
    try {
        const response = await fetch('/api/weather');
        if (response.ok) {
            const weatherData = await response.json();
            const weatherInfo = document.getElementById('weather-info');
            const weatherImg = document.getElementById('weather-img');

            const temperature = weatherData.temp;
            const condition = weatherData.sky;
            const time = weatherData.time;

            let formattedTime = `${time}:00`;

            weatherInfo.textContent = `${temperature}°C  ${condition}  (${formattedTime}시 기준)`;

            let imgSrc = '';
            switch (condition) {
                case '맑음':
                    imgSrc = '/images/sunny.png';
                    break;
                case '비':
                    imgSrc = '/images/rainy.png';
                    break;
                case '흐림':
                    imgSrc = '/images/cloudy.png';
                    break;
                case '구름':
                    imgSrc = '/images/partly_cloudy.png';
                    break;
                default:
                    imgSrc = '/images/default.png';
            }

            weatherImg.src = imgSrc;
            weatherImg.alt = condition;
        } else {
            console.error('Failed to fetch weather data');
        }
    } catch (error) {
        console.error('Error:', error);
    }
}

document.addEventListener('DOMContentLoaded', () => {
    fetchOrders();
    updateTime();
    updateWeather();

    setInterval(fetchOrders, 5000);
    setInterval(updateTime, 1000);
    setInterval(updateWeather, 600000);
});

// Existing JS functions
function fetchTableData(tableId, url) {
    fetch(url)
        .then(response => response.json())
        .then(data => {
            const tbody = document.getElementById(`${tableId}-body`);
            tbody.innerHTML = '';

            data.forEach(item => {
                const row = document.createElement('tr');

                const keys = Object.keys(item);
                keys.forEach(key => {
                    const cell = document.createElement('td');
                    cell.textContent = item[key];
                    row.appendChild(cell);
                });

                tbody.appendChild(row);
            });
        })
        .catch(error => console.error('Error fetching data:', error));
}

document.addEventListener('DOMContentLoaded', () => {
    const tabs = document.querySelectorAll('.tab');
    const tables = document.querySelectorAll('.table-container');

    tabs.forEach(tab => {
        tab.addEventListener('click', () => {
            tabs.forEach(t => t.classList.remove('active'));
            tab.classList.add('active');

            tables.forEach(table => table.classList.remove('active'));
            const tableId = `table-${tab.id.split('-')[1]}`;
            document.getElementById(tableId).classList.add('active');

            const endpoint = tab.id.split('-')[1];
            const apiUrl = `/api/tables/${endpoint}`;
            fetchTableData(`${endpoint}`, apiUrl);
        });
    });

    const initialTab = tabs[0];
    const initialEndpoint = initialTab.id.split('-')[1];
    const initialApiUrl = `/api/tables/${initialEndpoint}`;
    fetchTableData(`${initialEndpoint}`, initialApiUrl);
});
