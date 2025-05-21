import React from 'react';
import styles from '../../assets/css/ResidentDashboard.module.css';

const ServiceTab = () => {
  const user = JSON.parse(localStorage.getItem('user'));
  const residentId = user?.residentId;

  const [services, setServices] = React.useState([]);
  const [myServices, setMyServices] = React.useState([]);
  const [showMyServices, setShowMyServices] = React.useState(false);
  const [registerQuantity, setRegisterQuantity] = React.useState({}); // {serviceId: quantity}
  const handleLogout = () => {
    localStorage.removeItem('user');
    window.location.href = '/login';
  };

  // Lấy danh sách dịch vụ tòa nhà
  React.useEffect(() => {
    fetch('http://localhost:8080/api/service')
      .then(res => res.json())
      .then(data => setServices(data));
  }, []);

  // Lấy dịch vụ đã đăng ký
  const fetchMyServices = () => {
    if (!residentId) {
      alert('Không tìm thấy residentId!');
      return;
    }
    fetch(`http://localhost:8080/api/service-registrations/resident/${residentId}`)
      .then(res => res.json())
      .then(data => {
        console.log('Dịch vụ đã đăng ký:', data);
        setMyServices(Array.isArray(data) ? data : []);
      });
  };

  // Đăng ký dịch vụ
  const handleRegister = (service) => {
    let quantity = 1;
    if (service.serviceId === 1 || service.serviceId === 2) {
      quantity = registerQuantity[service.serviceId] || 1;
      if (!quantity || quantity < 1) {
        alert('Vui lòng nhập số lượng hợp lệ!');
        return;
      }
    }
    fetch('http://localhost:8080/api/service-registrations/register', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        residentId,
        serviceId: service.serviceId,
        quantity
      })
    })
      .then(res => {
        if (res.ok) {
          alert('Đăng ký thành công!');
        } else {
          alert('Đăng ký thất bại!');
        }
      });
  };

  return (
    <div className={styles.dashboard}>
      {/* Sidebar */}
      <div className={styles.sidebar}>
        <h4 className={styles.sidebarTitle}>Trang cư dân</h4>
        <nav className="nav flex-column">
          <a className={`nav-link ${styles.navLink} ${styles.navLinkActive}`} href="/resident">
            Thông tin cá nhân
          </a>
          <a className={`nav-link ${styles.navLink}`} href="/service">
            Dịch vụ
          </a>
        </nav>
        <button
          onClick={handleLogout}
          className={`btn btn-danger ${styles.logoutBtn}`}
        >
          Đăng xuất
        </button>
      </div>
      {/* Main Content */}
      <div className={styles.mainContent}>
        <div className={styles['service-layout']}>
          {/* Dịch vụ tòa nhà */}
          <div className={styles['service-list']}>
            <h3>Dịch vụ tòa nhà</h3>
            <div className={styles['service-grid']}>
              {services.map(s => (
                <div className="service-card" key={s.serviceId}>
                  <b>{s.serviceName}</b>
                  <div>{s.description}</div>
                  <div>Phí: {s.fee}đ</div>
                  <div className="service-action">
                    {(s.serviceId === 1 || s.serviceId === 2) && (
                      <input
                        type="number"
                        min={1}
                        style={{ width: 60,marginRight: 12 }}
                        value={registerQuantity[s.serviceId] || ''}
                        onChange={e =>
                          setRegisterQuantity(q => ({
                            ...q,
                            [s.serviceId]: e.target.value
                          }))
                        }
                        placeholder="Số lượng"
                      />
                    )}
                    <button
                      className="btn btn-primary btn-sm"
                      onClick={() => handleRegister(s)}
                    >Đăng ký</button>
                  </div>
                </div>
              ))}
            </div>
          </div>
          {/* Nút dịch vụ đã đăng ký */}
          <div style={{ alignSelf: 'flex-start' }}>
            <button className="btn btn-info" style={{ minWidth: 180 }} onClick={() => {
              setShowMyServices(true);
              fetchMyServices();
            }}>
              Dịch vụ đã đăng ký
            </button>
          </div>
        </div>

        {/* Popup dịch vụ đã đăng ký */}
        {showMyServices && (
          <div className={styles['service-popup-overlay']}>
            <div className={styles['service-popup']}>
              <button className={styles['service-popup-close']} onClick={() => setShowMyServices(false)}>&times;</button>
              <h4>Dịch vụ đã đăng ký</h4>
              {myServices.length === 0 && <div>Bạn chưa đăng ký dịch vụ nào.</div>}
              {myServices.map(ms => (
                <div className="service-card" key={ms.srId}>
                  <b>{ms.serviceName}</b>
                  <div>{ms.description}</div>
                  <div>Phí: {ms.fee}đ</div>
                  <div>Số lượng: {ms.quantity}</div>
                  <div className="service-action">
                    {(ms.serviceId === 1 || ms.serviceId === 2) && (
                      <>
                        <input
                          type="number"
                          value={ms.quantity}
                          min={1}
                          onChange={e => {
                            setMyServices(list => list.map(item =>
                              item.srId === ms.srId
                                ? { ...item, quantity: e.target.value }
                                : item
                            ));
                          }}
                          style={{ width: 60, marginRight: 8 }}
                        />
                        <button
                          className="btn btn-success btn-sm"
                          onClick={() => {
                            fetch(`http://localhost:8080/api/registered-services/${ms.srId}`, {
                              method: 'PATCH',
                              headers: { 'Content-Type': 'application/json' },
                              body: JSON.stringify({ quantity: ms.quantity })
                            }).then(() => alert('Cập nhật thành công!'));
                          }}
                        >Lưu</button>
                      </>
                    )}
                    <button
                      className="btn btn-danger btn-sm ms-2"
                      onClick={() => {
                        fetch(`http://localhost:8080/api/registered-services/${ms.srId}`, {
                          method: 'DELETE'
                        }).then(() => {
                          setMyServices(list => list.filter(item => item.srId !== ms.srId));
                          alert('Đã hủy dịch vụ!');
                        });
                      }}
                    >Hủy</button>
                  </div>
                </div>
              ))}
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

export default ServiceTab;