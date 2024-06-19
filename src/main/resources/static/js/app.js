function fetchTableData(tableId, url) {
    fetch(url)
        .then(response => response.json())
        .then(data => {
            const tbody = document.getElementById(`${tableId}-body`);
            tbody.innerHTML = '';

            data.forEach(item => {
                const row = document.createElement('tr');

                // 여기에서 적절한 컬럼을 생성하도록 합니다. 예를 들어:
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

            // 탭을 클릭할 때마다 해당 데이터를 로드합니다.
            const endpoint = tab.id.split('-')[1]; // tab-coffees, tab-complete-orders 등
            const apiUrl = `/api/tables/${endpoint}`;
            fetchTableData(`${endpoint}`, apiUrl);
        });
    });

    // 초기 페이지 로드 시 첫 번째 탭 데이터를 로드합니다.
    const initialTab = tabs[0]; // 첫 번째 탭
    const initialEndpoint = initialTab.id.split('-')[1]; // tab-coffees, tab-complete-orders 등
    const initialApiUrl = `/api/tables/${initialEndpoint}`;
    fetchTableData(`${initialEndpoint}`, initialApiUrl);
});
