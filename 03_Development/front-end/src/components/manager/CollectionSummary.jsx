import React, { useEffect, useState } from "react";

const CollectionSummary = () => {
  const [summary, setSummary] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const [selectedCpName, setSelectedCpName] = useState(null);
  const [details, setDetails] = useState(null);
  const [detailsLoading, setDetailsLoading] = useState(false);
  const [detailsError, setDetailsError] = useState(null);

  useEffect(() => {
    fetch("http://localhost:8080/api/collection-summary")
      .then((res) => {
        if (!res.ok) throw new Error("Failed to fetch");
        return res.json();
      })
      .then((data) => {
        setSummary(data);
        setLoading(false);
      })
      .catch((err) => {
        setError(err.message);
        setLoading(false);
      });
  }, []);

  const handleCpNameClick = (cpName) => {
    setSelectedCpName(cpName);
    setDetailsLoading(true);
    setDetailsError(null);
    fetch(`http://localhost:8080/api/payment-details?cpName=${encodeURIComponent(cpName)}`)
      .then((res) => {
        if (!res.ok) throw new Error("Failed to fetch details");
        return res.json();
      })
      .then((data) => {
        setDetails(data);
        setDetailsLoading(false);
      })
      .catch((err) => {
        setDetailsError(err.message);
        setDetailsLoading(false);
      });
  };

  if (loading) return (
    <div className="loading-container">
      <div className="spinner"></div>
      <p>Đang tải dữ liệu...</p>
    </div>
  );
  
  if (error) return (
    <div className="error-message">
      <p>Lỗi: {error}</p>
      <button onClick={() => window.location.reload()} className="retry-button">
        Thử lại
      </button>
    </div>
  );

  return (
    <div className="collection-container">
      <h2 className="page-title">Thống kê đợt thu</h2>
      
      {summary ? (
        <>
          <div className="summary-table-container">
            <table className="summary-table">
              <thead>
                <tr>
                  <th>Đợt thu</th>
                  <th>Tổng tiền đã thu</th>
                  <th>Tổng tiền chưa thu</th>
                  <th>Số hộ đã nộp</th>
                  <th>Số hộ chưa nộp</th>
                </tr>
              </thead>
              <tbody>
                {summary.map((item) => (
                  <tr key={item.cpId || item.cpName} className={selectedCpName === item.cpName ? 'active-row' : ''}>
                    <td>
                      <button
                        className="collection-period-button"
                        onClick={() => handleCpNameClick(item.cpName)}
                      >
                        {item.cpName}
                      </button>
                    </td>
                    <td className="paid">{item.totalPaid.toLocaleString('vi-VN')}₫</td>
                    <td className="unpaid">{item.totalUnpaid.toLocaleString('vi-VN')}₫</td>
                    <td className="paid">{item.paidHouseholds}</td>
                    <td className="unpaid">{item.unpaidHouseholds}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>

          {selectedCpName && (
            <div className="details-container">
              <h3 className="details-title">
                Chi tiết đợt thu: <span className="collection-period-name">{selectedCpName}</span>
              </h3>
              
              {detailsLoading && (
                <div className="loading-container">
                  <div className="spinner small"></div>
                  <p>Đang tải chi tiết...</p>
                </div>
              )}
              
              {detailsError && (
                <div className="error-message">
                  <p>Lỗi: {detailsError}</p>
                  <button 
                    onClick={() => handleCpNameClick(selectedCpName)} 
                    className="retry-button"
                  >
                    Thử lại
                  </button>
                </div>
              )}
              
              {details && details.length > 0 ? (
                <div className="details-table-container">
                  <table className="details-table">
                    <thead>
                      <tr>
                        <th>Mã hộ</th>
                        <th>Trạng thái</th>
                        <th>Số tiền</th>
                        <th>Ngày thu</th>
                      </tr>
                    </thead>
                    <tbody>
                      {details.map((d, index) => (
                        <tr key={`${d.citizenId}-${index}`} className={d.status === 'Đã thu' ? 'paid-row' : 'unpaid-row'}>
                          <td>{d.citizenId}</td>
                          <td>
                            <span className={`status-badge ${d.status === 'Đã thu' ? 'paid' : 'unpaid'}`}>
                              {d.status}
                            </span>
                          </td>
                          <td>{d.cost ? d.cost.toLocaleString('vi-VN') + '₫' : 'N/A'}</td>
                          <td>
                            {d.paymentDate
                              ? new Date(d.paymentDate).toLocaleDateString("vi-VN")
                              : "Chưa thu"}
                          </td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                </div>
              ) : (
                !detailsLoading && <p className="no-data">Không có dữ liệu chi tiết</p>
              )}
            </div>
          )}
        </>
      ) : (
        <p className="no-data">Không có dữ liệu</p>
      )}
    </div>
  );
};

export default CollectionSummary;

// CSS Styles
const styles = `
  .collection-container {
    font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
    max-width: 1200px;
    margin: 0 auto;
    padding: 20px;
    color: #333;
  }
  
  .page-title {
    color: #2c3e50;
    text-align: center;
    margin-bottom: 30px;
    font-weight: 600;
    border-bottom: 2px solid #3498db;
    padding-bottom: 10px;
  }
  
  .loading-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    height: 200px;
  }
  
  .spinner {
    border: 5px solid #f3f3f3;
    border-top: 5px solid #3498db;
    border-radius: 50%;
    width: 50px;
    height: 50px;
    animation: spin 1s linear infinite;
    margin-bottom: 15px;
  }
  
  .spinner.small {
    width: 30px;
    height: 30px;
    border-width: 3px;
  }
  
  @keyframes spin {
    0% { transform: rotate(0deg); }
    100% { transform: rotate(360deg); }
  }
  
  .error-message {
    background-color: #f8d7da;
    color: #721c24;
    padding: 15px;
    border-radius: 5px;
    margin: 20px 0;
    text-align: center;
  }
  
  .retry-button {
    background-color: #dc3545;
    color: white;
    border: none;
    padding: 8px 15px;
    border-radius: 4px;
    cursor: pointer;
    margin-top: 10px;
    transition: background-color 0.3s;
  }
  
  .retry-button:hover {
    background-color: #c82333;
  }
  
  .summary-table-container {
    overflow-x: auto;
    margin-bottom: 40px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
    border-radius: 8px;
  }
  
  .summary-table {
    width: 100%;
    border-collapse: collapse;
    background-color: white;
  }
  
  .summary-table th {
    background-color: #3498db;
    color: white;
    padding: 12px 15px;
    text-align: left;
  }
  
  .summary-table td {
    padding: 12px 15px;
    border-bottom: 1px solid #e0e0e0;
  }
  
  .summary-table tr:hover {
    background-color: #f5f5f5;
  }
  
  .active-row {
    background-color: #e3f2fd !important;
  }
  
  .collection-period-button {
    background: none;
    border: none;
    color: #3498db;
    cursor: pointer;
    font-weight: 500;
    padding: 5px;
    text-align: left;
    transition: color 0.3s;
  }
  
  .collection-period-button:hover {
    color: #1a5276;
    text-decoration: underline;
  }
  
  .paid {
    color: #27ae60;
    font-weight: 500;
  }
  
  .unpaid {
    color: #e74c3c;
    font-weight: 500;
  }
  
  .details-container {
    margin-top: 30px;
    animation: fadeIn 0.5s ease-in;
  }
  
  @keyframes fadeIn {
    from { opacity: 0; transform: translateY(10px); }
    to { opacity: 1; transform: translateY(0); }
  }
  
  .details-title {
    color: #2c3e50;
    margin-bottom: 20px;
    font-weight: 500;
  }
  
  .collection-period-name {
    color: #3498db;
    font-weight: 600;
  }
  
  .details-table-container {
    overflow-x: auto;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
    border-radius: 8px;
  }
  
  .details-table {
    width: 100%;
    border-collapse: collapse;
    background-color: white;
  }
  
  .details-table th {
    background-color: #2c3e50;
    color: white;
    padding: 12px 15px;
    text-align: left;
  }
  
  .details-table td {
    padding: 12px 15px;
    border-bottom: 1px solid #e0e0e0;
  }
  
  .paid-row {
    background-color: #e8f5e9;
  }
  
  .unpaid-row {
    background-color: #ffebee;
  }
  
  .status-badge {
    display: inline-block;
    padding: 4px 8px;
    border-radius: 12px;
    font-size: 0.85em;
    font-weight: 500;
  }
  
  .status-badge.paid {
    background-color: #c8e6c9;
    color: #1b5e20;
  }
  
  .status-badge.unpaid {
    background-color: #ffcdd2;
    color: #c62828;
  }
  
  .no-data {
    text-align: center;
    color: #7f8c8d;
    padding: 20px;
    background-color: #f9f9f9;
    border-radius: 5px;
  }
`;

// Add styles to the document head
const styleElement = document.createElement('style');
styleElement.innerHTML = styles;
document.head.appendChild(styleElement);