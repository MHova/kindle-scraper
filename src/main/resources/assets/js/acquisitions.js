(async function() {
  new Chart(
    document.getElementById('acquisitions'),
    {
      type: 'line',
      data: {
        labels: ["Hello", "World", "Goodbye"],
        datasets: [
          {
            label: 'Price over time',
            data: [8, 9, 10],
            borderColor: 'blue'
          }
        ]
      },
      options: {
        responsive: true,
        plugins: {
            legend: {
                position: 'top'
            },
            title: {
                display: true,
                text: 'Amazon Fire HD 10 Tablet Price Over Time'
            }
        }
      }
    }
  );
})();
