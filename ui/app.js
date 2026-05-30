const API_URL = 'http://localhost:8081/api';

document.addEventListener('DOMContentLoaded', () => {
    fetchRooms();

    document.getElementById('search-btn').addEventListener('click', () => {
        const category = document.getElementById('category-select').value;
        fetchRooms(category);
    });
    
    // Auto-filter when category changes
    document.getElementById('category-select').addEventListener('change', (e) => {
        fetchRooms(e.target.value);
    });

    document.getElementById('booking-form').addEventListener('submit', handleBooking);
    
    // Set min date for date pickers to today
    const today = new Date().toISOString().split('T')[0];
    document.getElementById('check-in').setAttribute('min', today);
    
    document.getElementById('check-in').addEventListener('change', (e) => {
        // Checkout must be at least 1 day after check in
        let checkoutMin = new Date(e.target.value);
        checkoutMin.setDate(checkoutMin.getDate() + 1);
        document.getElementById('check-out').setAttribute('min', checkoutMin.toISOString().split('T')[0]);
    });

    // Account Dropdown Logic
    const accountBtn = document.getElementById('account-btn');
    const accountDropdown = document.getElementById('account-dropdown');
    
    accountBtn.addEventListener('click', (e) => {
        e.stopPropagation();
        accountDropdown.classList.toggle('hidden');
        if(!accountDropdown.classList.contains('hidden')) {
            accountDropdown.classList.add('animate-fade-in-up');
        }
    });

    // Close dropdown when clicking outside
    document.addEventListener('click', (e) => {
        if (!accountBtn.contains(e.target) && !accountDropdown.contains(e.target)) {
            accountDropdown.classList.add('hidden');
        }
    });
});

async function fetchRooms(category = 'ALL') {
    const container = document.getElementById('rooms-container');
    container.innerHTML = '<p class="text-on-surface-variant col-span-full">Loading luxurious spaces...</p>';

    try {
        let url = `${API_URL}/rooms`;
        if (category !== 'ALL') {
            url += `?category=${category}`;
        }

        const response = await fetch(url);
        if (!response.ok) throw new Error('Failed to fetch');
        
        const rooms = await response.json();
        renderRooms(rooms);
    } catch (error) {
        console.error('Error fetching rooms:', error);
        container.innerHTML = '<p class="text-error col-span-full">Failed to load rooms. Please ensure the backend server is running on port 8081.</p>';
    }
}

function renderRooms(rooms) {
    const container = document.getElementById('rooms-container');
    container.innerHTML = '';

    if (rooms.length === 0) {
        container.innerHTML = '<p class="text-on-surface-variant col-span-full">No rooms available in this category.</p>';
        return;
    }

    rooms.forEach((room, index) => {
        const card = document.createElement('div');
        card.className = 'bg-surface-container-lowest p-6 rounded-xl border border-outline-variant academic-card room-card-hover flex flex-col gap-4 animate-fade-in-up';
        card.style.animationDelay = `${index * 0.1}s`;
        
        let typeClass = 'type-standard';
        let imgSrc = `assets/${room.roomId}.png`;
        if(room.type === 'DELUXE') {
            typeClass = 'type-deluxe';
        }
        if(room.type === 'SUITE') {
            typeClass = 'type-suite';
        }

        card.innerHTML = `
            <div class="h-40 w-full rounded-lg overflow-hidden bg-surface-container-high mb-2">
                <img src="${imgSrc}" alt="Room ${room.roomNumber}" class="w-full h-full object-cover"/>
            </div>
            <div class="flex justify-between items-start mt-2">
                <span class="grade-chip ${typeClass}">${room.type} CLASS</span>
                <span class="material-symbols-outlined text-outline">king_bed</span>
            </div>
            <div>
                <h3 class="font-headline-lg text-headline-md text-primary">Room ${room.roomNumber}</h3>
                <div class="font-display-metric text-[32px] text-secondary-container leading-tight mt-2">$${room.baseRate} <span class="text-body-sm text-on-surface-variant">/ night</span></div>
            </div>
            <button class="mt-auto bg-surface-container-highest text-on-surface hover:bg-surface-dim py-3 rounded-lg font-headline-md text-body-sm transition-colors active:scale-95" onclick="openBookingModal('${room.roomId}', '${room.type}', ${room.baseRate})">
                Reserve Now
            </button>
        `;
        container.appendChild(card);
    });
}

function openBookingModal(roomId, type, rate) {
    document.getElementById('room-id').value = roomId;
    document.getElementById('modal-room-info').innerHTML = `<strong>Booking:</strong> ${type} Class Room <br><strong>Rate:</strong> $${rate} per night`;
    
    document.getElementById('booking-form').classList.remove('hidden');
    document.getElementById('booking-success').classList.add('hidden');
    
    const modal = document.getElementById('booking-modal');
    modal.classList.remove('hidden');
    
    // Trigger animation
    setTimeout(() => {
        modal.classList.remove('opacity-0');
        document.getElementById('modal-content').classList.remove('scale-95');
    }, 10);
}

function closeModal() {
    const modal = document.getElementById('booking-modal');
    modal.classList.add('opacity-0');
    document.getElementById('modal-content').classList.add('scale-95');
    
    setTimeout(() => {
        modal.classList.add('hidden');
        document.getElementById('booking-form').reset();
        fetchRooms(); // Refresh room list in case availability changed
    }, 300);
}

async function handleBooking(e) {
    e.preventDefault();
    
    const payload = {
        roomId: document.getElementById('room-id').value,
        userName: document.getElementById('user-name').value,
        userEmail: document.getElementById('user-email').value,
        userPhone: document.getElementById('user-phone').value,
        checkInDate: document.getElementById('check-in').value,
        checkOutDate: document.getElementById('check-out').value
    };

    const submitBtn = document.getElementById('submit-btn');
    const originalText = submitBtn.innerHTML;
    submitBtn.innerHTML = '<span class="material-symbols-outlined animate-spin">hourglass_empty</span> Processing...';
    submitBtn.disabled = true;

    try {
        const response = await fetch(`${API_URL}/book`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(payload)
        });

        const result = await response.json();

        if (!response.ok) {
            throw new Error(result.error || 'Booking failed');
        }

        // Show Success UI
        document.getElementById('booking-form').classList.add('hidden');
        const successDiv = document.getElementById('booking-success');
        successDiv.classList.remove('hidden');
        successDiv.classList.add('animate-fade-in-up');
        
        document.getElementById('success-details').innerText = 
            `Booking ID: ${result.reservationId}\nTotal Charged: $${result.totalAmount}`;
            
    } catch (error) {
        alert(error.message);
    } finally {
        submitBtn.innerHTML = originalText;
        submitBtn.disabled = false;
    }
}
