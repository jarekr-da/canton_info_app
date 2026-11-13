import { useEffect, useState } from 'react'

function App() {
  const [now, setNow] = useState(() => new Date())

  useEffect(() => {
    const id = window.setInterval(() => setNow(new Date()), 1000)
    return () => window.clearInterval(id)
  }, [])

  return (
    <main className="screen">
      <h1>Hello, Canton!</h1>
      <p className="clock">{now.toLocaleTimeString()}</p>
    </main>
  )
}

export default App
