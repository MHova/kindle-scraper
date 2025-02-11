const getThirtyFiveMostRecentPriceChecks = async () =>
    await (await fetch('http://localhost:8080/api/pricechecks?limit=35')).json();

(async function() {
  const results = await getThirtyFiveMostRecentPriceChecks();
  const labels = results.map(pc => pc.timestamp);
  const data = results.map(pc => pc.price);

  new Chart(
    document.getElementById('pricechecks'),
    {
      type: 'line',
      data: {
        labels: labels,
        datasets: [
          {
            label: 'Price over time',
            data: data,
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
