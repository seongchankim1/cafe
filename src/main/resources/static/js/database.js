document.addEventListener('DOMContentLoaded', () => {
    const tabs = document.querySelectorAll('.tab');
    const tabContents = document.querySelectorAll('.tab-content');

    tabs.forEach(tab => {
        tab.addEventListener('click', () => {
            tabs.forEach(t => t.classList.remove('active'));
            tab.classList.add('active');

            tabContents.forEach(tc => tc.classList.remove('active'));
            document.getElementById(tab.dataset.tab).classList.add('active');

            // 각 탭에 맞는 데이터를 로드
            if (tab.dataset.tab === 'complete-orders') {
                fetchCompleteOrders();
            } else if (tab.dataset.tab === 'users') {
                fetchUsers();
            }
        });
    });

    // 페이지 로드 시 기본으로 완료된 주문 데이터를 로드
    fetchCompleteOrders();
});

function fetchCompleteOrders() {
    fetch('/api/database/complete')
        .then(response => response.json())
        .then(data => {
            const tableBody = document.querySelector('#complete-orders-table tbody');
            tableBody.innerHTML = '';

            data.forEach(order => {
                const row = document.createElement('tr');
                row.innerHTML = `
                        <td>${order.completeOrderId}</td>
                        <td>${new Date(order.createdAt).toLocaleString()}</td>
                        <td>${order.coffeeName}</td>
                        <td>${order.price.toLocaleString()}원</td>
                        <td>${order.username}</td>
                        <td>${order.strength}</td>
                    `;
                tableBody.appendChild(row);
            });

            document.querySelector('#complete-orders .loading').style.display = 'none';
        })
        .catch(err => {
            console.error('Failed to fetch complete orders:', err);
            document.querySelector('#complete-orders .loading').textContent = '데이터를 가져오는 데 실패했습니다.';
        });
}

function fetchUsers() {
    fetch('/api/database/user')
        .then(response => response.json())
        .then(data => {
            const tableBody = document.querySelector('#users-table tbody');
            tableBody.innerHTML = '';

            data.forEach(user => {
                const row = document.createElement('tr');
                row.innerHTML = `
                        <td><input type="radio" name="userSelect" value="${user.id}"></td>
                        <td>${user.id}</td>
                        <td>${new Date(user.createdAt).toLocaleString()}</td>
                        <td>${user.username}</td>
                        <td>${user.money.toLocaleString()}원</td>
                        <td>${user.phoneNumber}</td>
                        <td>${user.role}</td>
                    `;
                tableBody.appendChild(row);
            });

            document.querySelector('#users .loading').style.display = 'none';
        })
        .catch(err => {
            console.error('Failed to fetch users:', err);
            document.querySelector('#users .loading').textContent = '데이터를 가져오는 데 실패했습니다.';
        });
}

function openModal() {
    const selectedUser = document.querySelector('input[name="userSelect"]:checked');
    if (!selectedUser) {
        alert('사용자를 선택하세요.');
        return;
    }
    document.getElementById('addMoneyModal').style.display = 'flex';
}

function closeModal() {
    document.getElementById('addMoneyModal').style.display = 'none';
}

function addMoney() {
    const selectedUser = document.querySelector('input[name="userSelect"]:checked');
    const amount = document.getElementById('amount').value;

    if (!selectedUser || !amount) {
        alert('정확한 금액을 입력하세요.');
        return;
    }

    const userId = parseInt(selectedUser.value, 10);

    console.log('Selected user ID:', userId); // 디버깅용 로그
    console.log('Amount:', amount); // 디버깅용 로그

    fetch('/api/database/money', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ userId: userId, amount: parseInt(amount) })
    })
        .then(response => {
            if (response.ok) {
                alert('충전 완료!');
                closeModal();
                location.reload();
            } else {
                alert('충전 실패. 다시 시도하세요.');
            }
        })
        .catch(err => {
            console.error('Failed to add money:', err);
            alert('충전 처리 중 오류가 발생했습니다. 다시 시도해주세요.');
        });
}
