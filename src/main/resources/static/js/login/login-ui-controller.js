
/*login-modal*/
const loginModal = document.getElementById("passwordChangeNotificationModal");
const loginConfirmBtn = document.getElementById("loginConfirmBtn");
loginConfirmBtn.onclick = () => {
    loginModal.style.display = "none";
};