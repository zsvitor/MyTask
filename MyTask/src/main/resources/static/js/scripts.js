document.addEventListener('DOMContentLoaded', function() {
    var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });
    setTimeout(function() {
        var alerts = document.querySelectorAll('.alert');
        alerts.forEach(function(alert) {
            var bsAlert = new bootstrap.Alert(alert);
            bsAlert.close();
        });
    }, 5000);
    var filterForm = document.querySelector('#task-filter-form');
    if (filterForm) {
        var selects = filterForm.querySelectorAll('select');
        selects.forEach(function(select) {
            select.addEventListener('change', function() {
                filterForm.submit();
            });
        });     
        var dateInputs = filterForm.querySelectorAll('input[type="date"]');
        dateInputs.forEach(function(input) {
            input.addEventListener('change', function() {
                filterForm.submit();
            });
        });
    }
});