import React, { useEffect, useState } from 'react';
import axios from 'axios';

const API_URL = 'http://localhost:8080/api/users';

const UserCRUD = () => {
  const [users, setUsers] = useState([]);
  const [form, setForm] = useState({
    username: '',
    email: '',
    phone: '',
    fullname: '',
    roleName: 'citizen',
    password: ''
  });
  const [editingUserId, setEditingUserId] = useState(null);

  useEffect(() => {
    fetchUsers();
  }, []);

  const fetchUsers = async () => {
    try {
      const res = await axios.get(API_URL);
      setUsers(res.data);
    } catch (error) {
      console.error('Lỗi khi tải danh sách người dùng:', error);
    }
  };

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (editingUserId) {
        await axios.put(`${API_URL}/${editingUserId}`, form);
      } else {
        await axios.post(API_URL, form);
      }
      fetchUsers();
      setForm({ username: '', email: '', phone: '', fullname: '', roleName: 'citizen', password: '' });
      setEditingUserId(null);
    } catch (error) {
      console.error('Lỗi khi lưu người dùng:', error);
    }
  };

  const handleEdit = (user) => {
    setForm({
      username: user.username,
      email: user.email,
      phone: user.phone,
      fullname: user.fullname,
      roleName: user.roleName,
      password: '' // Không hiển thị mật khẩu, người dùng nhập lại nếu cần đổi
    });
    setEditingUserId(user.userId);
  };

  const handleDelete = async (id) => {
    if (window.confirm('Bạn có chắc chắn muốn xóa người dùng này?')) {
      try {
        await axios.delete(`${API_URL}/${id}`);
        fetchUsers();
      } catch (error) {
        console.error('Lỗi khi xóa người dùng:', error);
      }
    }
  };

  return (
    <div className="container mt-4">
      <h2>Quản lý người dùng</h2>

      <form onSubmit={handleSubmit} className="row g-3">
        <div className="col-md-3">
          <input type="text" name="username" value={form.username} onChange={handleChange} className="form-control" placeholder="Tên đăng nhập" required />
        </div>
        <div className="col-md-3">
          <input type="email" name="email" value={form.email} onChange={handleChange} className="form-control" placeholder="Email" required />
        </div>
        <div className="col-md-3">
          <input type="text" name="phone" value={form.phone} onChange={handleChange} className="form-control" placeholder="Số điện thoại" required />
        </div>
        <div className="col-md-3">
          <input type="text" name="fullname" value={form.fullname} onChange={handleChange} className="form-control" placeholder="Họ tên" required />
        </div>
        <div className="col-md-3">
          <select name="roleName" value={form.roleName} onChange={handleChange} className="form-select" required>
            <option value="citizen">Citizen</option>
            <option value="accountant">Accountant</option>
            <option value="manager">Manager</option>
          </select>
        </div>
        {!editingUserId && (
          <div className="col-md-3">
            <input type="password" name="password" value={form.password} onChange={handleChange} className="form-control" placeholder="Mật khẩu" required />
          </div>
        )}
        <div className="col-md-3">
          <button type="submit" className="btn btn-primary w-100">
            {editingUserId ? 'Cập nhật' : 'Thêm mới'}
          </button>
        </div>
      </form>
      <div className="mt-4"></div>
        <div style={{ maxHeight: '800px', overflowY: 'auto', border: '1px solid #ccc' }}>
      <table className="table table-bordered table-striped mt-4">
        <thead className="table-dark">
          <tr>
            <th>ID</th>
            <th>Tên đăng nhập</th>
            <th>Email</th>
            <th>Số điện thoại</th>
            <th>Họ tên</th>
            <th>Vai trò</th>
            <th>Ngày tạo</th>
            <th>Ngày cập nhật</th>
            <th>Thao tác</th>
          </tr>
        </thead>
        <tbody>
          {users.map((u) => (
            <tr key={u.userId}>
              <td>{u.userId}</td>
              <td>{u.username}</td>
              <td>{u.email}</td>
              <td>{u.phone}</td>
              <td>{u.fullname}</td>
              <td>{u.roleName}</td>
              <td>{new Date(u.createdAt).toLocaleDateString()}</td>
              <td>{new Date(u.updatedAt).toLocaleDateString()}</td>
              <td>
                <button className="btn btn-warning btn-sm me-2" onClick={() => handleEdit(u)}>Sửa</button>
                <button className="btn btn-danger btn-sm" onClick={() => handleDelete(u.userId)}>Xóa</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
      </div>
    </div>
  );
};

export default UserCRUD;
