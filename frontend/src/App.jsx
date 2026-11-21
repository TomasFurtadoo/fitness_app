import { useEffect, useState } from "react";
import { getFoods } from "./api/foodApi";

function App() {
  const [foods, setFoods] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    getFoods()
      .then(setFoods)
      .catch((err) =>
        setError(err.response?.data?.mensagem ?? "Nao foi possivel contactar o servidor.")
      )
      .finally(() => setLoading(false));
  }, []);

  if (loading) return <p>A carregar...</p>;
  if (error) return <p>Erro: {error}</p>;

  return (
    <div>
      <h1>Alimentos</h1>
      <ul>
        {foods.map((f) => (
          <li key={f.id}>
            {f.nome}: {f.calorias100g} kcal/100g
          </li>
        ))}
      </ul>
    </div>
  );
}

export default App;