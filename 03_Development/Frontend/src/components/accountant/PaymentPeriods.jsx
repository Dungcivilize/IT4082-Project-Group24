import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'

export default function PaymentPeriods() {
  const [periods, setPeriods] = useState([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    fetch('http://localhost:8080/api/accountant/payment-periods')
      .then(res => res.json())
      .then(data => {
        setPeriods(data)
        setLoading(false)
      })
      .catch(() => setLoading(false))
  }, [])

  if (loading) return <p>Đang tải danh sách đợt thu phí...</p>
  if (!periods.length) return <p>Chưa có đợt thu phí nào</p>

    return (
        <div>
        <h1>Danh sách đợt thu phí</h1>

        <Link to="/accountant/payment-periods/create">
            <button style={{ marginBottom: 20 }}>Tạo đợt thu phí mới</button>
        </Link>

        <ul style={{ padding: 0, listStyle: 'none' }}>
            {periods.map(p => (
            <li key={p.paymentPeriodId} style={{ marginBottom: 10 }}>
                <Link to={`/accountant/payment-periods/${p.paymentPeriodId}`}>
                Kỳ {p.month}/{p.year} - Trạng thái: {p.status}
                </Link>
                <p>{p.note}</p>
            </li>
            ))}
        </ul>
        </div>
    )
}
