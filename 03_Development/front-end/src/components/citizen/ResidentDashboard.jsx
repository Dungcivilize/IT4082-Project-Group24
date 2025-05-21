import React from 'react';
import { useNavigate } from 'react-router-dom';
import styles from '../../assets/css/ResidentDashboard.module.css';

const ResidentDashboard = () => {
  const navigate = useNavigate();
  const [user, setUser] = React.useState(() => JSON.parse(localStorage.getItem('user')));
  const [isEditing, setIsEditing] = React.useState(false);
  const [loading, setLoading] = React.useState(false);

  // Lưu giá trị gốc và giá trị đang chỉnh sửa
  const [original, setOriginal] = React.useState({
    fullname: user?.fullname || '',
    email: user?.email || user?.username || '',
    phone: user?.phone || '',
    cccdId: user?.cccdId || '',
    job: user?.job || '',
    dob: user?.dob ? user.dob.slice(0, 10) : ''
  });
  const [form, setForm] = React.useState({ ...original });

  // Lấy thông tin chi tiết khi vào trang
  React.useEffect(() => {
    // Nếu đã có đủ thông tin thì không cần fetch lại
    if (user && user.cccdId && user.job && user.dob && user.phone) return;

    // Nếu chưa có, fetch từ backend
    const fetchDetail = async () => {
      try {
        const res = await fetch(`http://localhost:8080/api/citizens/user/${user.userId}`);
        if (res.ok) {
          const data = await res.json();
          // Cập nhật lại localStorage và state, đảm bảo residentId không bị mất
          const newUser = { ...user, ...data };
          setUser(newUser);
          localStorage.setItem('user', JSON.stringify(newUser));
          setOriginal({
            fullname: newUser.fullname || '',
            email: newUser.email || newUser.username || '',
            phone: newUser.phone || '',
            cccdId: newUser.cccdId || '',
            job: newUser.job || '',
            dob: newUser.dob ? newUser.dob.slice(0, 10) : ''
          });
          setForm({
            fullname: newUser.fullname || '',
            email: newUser.email || newUser.username || '',
            phone: newUser.phone || '',
            cccdId: newUser.cccdId || '',
            job: newUser.job || '',
            dob: newUser.dob ? newUser.dob.slice(0, 10) : ''
          });
        }
      } catch (err) {
        alert('Không thể lấy thông tin chi tiết!');
      }
    };
    if (user?.userId) fetchDetail();
    // eslint-disable-next-line
  }, []);

  const handleLogout = () => {
    localStorage.removeItem('user');
    navigate('/login');
  };

  // Hàm lấy các trường đã thay đổi
  const getChangedFields = () => {
    const changed = {};
    Object.keys(form).forEach(key => {
      if (form[key] !== original[key]) {
        changed[key] = form[key];
      }
    });
    return changed;
  };

  // Hiển thị thông tin cá nhân hoặc form chỉnh sửa
  const renderUserInfo = () => {
    if (isEditing) {
      return (
        <form
          onSubmit={async (e) => {
            e.preventDefault();
            const changed = getChangedFields();
            if (Object.keys(changed).length === 0) {
              alert('Bạn chưa thay đổi thông tin nào!');
              setIsEditing(false);
              return;
            }
            setLoading(true);
            const res = await fetch(`http://localhost:8080/api/citizens/${user.citizenId}`, {
              method: 'PATCH',
              headers: { 'Content-Type': 'application/json' },
              body: JSON.stringify(changed)
            });
            setLoading(false);
            if (res.ok) {
              alert('Cập nhật thành công!');
              // Cập nhật lại localStorage và state
              const newUser = { ...user, ...changed };
              setUser(newUser);
              localStorage.setItem('user', JSON.stringify(newUser));
              setOriginal({ ...original, ...changed });
              setForm({ ...form, ...changed });
              setIsEditing(false);
            } else {
              alert('Có lỗi xảy ra!');
            }
          }}
        >
          <div className="mb-2">
            <label>Họ tên</label>
            <input className="form-control" value={form.fullname} onChange={e => setForm(f => ({ ...f, fullname: e.target.value }))} />
          </div>
          <div className="mb-2">
            <label>Email</label>
            <input className="form-control" value={form.email} onChange={e => setForm(f => ({ ...f, email: e.target.value }))} />
          </div>
          <div className="mb-2">
            <label>Số điện thoại</label>
            <input className="form-control" value={form.phone} onChange={e => setForm(f => ({ ...f, phone: e.target.value }))} />
          </div>
          <div className="mb-2">
            <label>CCCD</label>
            <input className="form-control" value={form.cccdId} onChange={e => setForm(f => ({ ...f, cccdId: e.target.value }))} />
          </div>
          <div className="mb-2">
            <label>Nghề nghiệp</label>
            <input className="form-control" value={form.job} onChange={e => setForm(f => ({ ...f, job: e.target.value }))} />
          </div>
          <div className="mb-2">
            <label>Ngày sinh</label>
            <input type="date" className="form-control" value={form.dob} onChange={e => setForm(f => ({ ...f, dob: e.target.value }))} />
          </div>
          <button className="btn btn-success me-2" type="submit" disabled={loading}>Lưu</button>
          <button className="btn btn-secondary" type="button" onClick={() => { setIsEditing(false); setForm(original); }}>Hủy</button>
        </form>
      );
    }
    return (
      <>
        <button className="btn btn-primary mb-3" onClick={() => setIsEditing(true)}>
          Chỉnh sửa thông tin
        </button>
        <h5 className="card-title">Thông tin người dùng</h5>
        <p className="card-text">
          <strong>Tên người dùng:</strong> {original.fullname}<br />
          <strong>Email:</strong> {original.email}<br />
          <strong>Số điện thoại:</strong> {original.phone}<br />
          <strong>CCCD:</strong> {original.cccdId}<br />
          <strong>Nghề nghiệp:</strong> {original.job}<br />
          <strong>Ngày sinh:</strong> {original.dob}<br />
          <strong>Vai trò:</strong> Cư dân<br />
          <strong>ID:</strong> {user?.userId}<br />
          <strong>Resident ID:</strong> {user?.residentId}
        </p>
      </>
    );
  };

  if (!user) return <div>Đang tải thông tin...</div>;

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
        <h2>Chào mừng bạn đến với hệ thống!</h2>
        <p>Chọn mục bên trái để xem thông tin chi tiết.</p>
        <div className="card mt-4">
          <div className="card-body">
            {renderUserInfo()}
          </div>
        </div>
      </div>
    </div>
  );
};

export default ResidentDashboard; 