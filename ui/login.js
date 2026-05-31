document.addEventListener('DOMContentLoaded', () => {
    const loginForm = document.getElementById('login-form');
    
    // Check if already logged in, if so redirect to index
    if (localStorage.getItem('aura_user')) {
        window.location.href = 'index.html';
    }

    loginForm.addEventListener('submit', (e) => {
        e.preventDefault();
        
        const name = document.getElementById('login-name').value;
        const email = document.getElementById('login-email').value;
        
        const btn = document.getElementById('login-btn');
        const originalText = btn.innerHTML;
        btn.innerHTML = '<span class="material-symbols-outlined animate-spin" style="font-size:20px;">hourglass_empty</span> Signing in...';
        btn.disabled = true;

        // Simulate network request
        setTimeout(() => {
            const user = {
                name: name,
                email: email
            };
            
            localStorage.setItem('aura_user', JSON.stringify(user));
            window.location.href = 'index.html';
        }, 800);
    });
});
