import React, { useEffect, useState } from 'react';
import axios from 'axios';
import './UserList.css';

const UserList = () => {
  const [users, setUsers] = useState([]);
  const [filteredUsers, setFilteredUsers] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');
  const [loading, setLoading] = useState(true);

  // State cho edit modal
  const [editingUser, setEditingUser] = useState(null);
  const [editForm, setEditForm] = useState({
    fullName: '',
    email: '',
    phone: '',
    role: '',
    password: '',
  });

  useEffect(() => {
    fetchUsers();
  }, []);

  const fetchUsers = async () => {
    try {
      const res = await axios.get('http://localhost:8080/api/users');
      setUsers(res.data);
      setFilteredUsers(res.data);
      setLoading(false);
    } catch (error) {
      console.error('Lỗi khi lấy danh sách người dùng', error);
      setLoading(false);
    }
  };

  const handleSearch = (e) => {
    const value = e.target.value.toLowerCase();
    setSearchTerm(value);
    const filtered = users.filter((user) =>
      user.fullName.toLowerCase().includes(value) ||
      user.email.toLowerCase().includes(value) ||
      user.phone.toLowerCase().includes(value)
    );
    setFilteredUsers(filtered);
  };

  const handleEdit = (id) => {
    const user = users.find((u) => u.userId === id);
    setEditingUser(id);
    setEditForm({
      fullName: user.fullName,
      email: user.email,
      phone: user.phone,
      role: user.role,
      password: '',
    });
  };

  const handleEditChange = (e) => {
    const { name, value } = e.target;
    setEditForm({ ...editForm, [name]: value });
  };

  const handleEditSubmit = async (e) => {
    e.preventDefault();
    try {
      await axios.put(`http://localhost:8080/api/users/${editingUser}`, editForm);
      alert('Cập nhật người dùng thành công!');
      setEditingUser(null);
      fetchUsers();
    } catch (error) {
      alert('Cập nhật thất bại!');
      console.error(error);
    }
  };

  const handleDelete = async (id) => {
    if (window.confirm('Bạn có chắc chắn muốn xóa người dùng này?')) {
      try {
        await axios.delete(`http://localhost:8080/api/users/${id}`);
        const newUsers = users.filter((user) => user.userId !== id);
        setUsers(newUsers);
        setFilteredUsers(
          newUsers.filter((user) =>
            user.fullName.toLowerCase().includes(searchTerm) ||
            user.email.toLowerCase().includes(searchTerm) ||
            user.phone.toLowerCase().includes(searchTerm)
          )
        );
      } catch (error) {
        alert('Xóa người dùng thất bại');
        console.error(error);
      }
    }
  };

  if (loading) return <p>Đang tải danh sách người dùng...</p>;

  return (
    <div className="userlist-container">
      <h2 className="userlist-title">Danh sách người dùng</h2>

      <input
        type="text"
        placeholder="Tìm kiếm theo tên, email hoặc số điện thoại..."
        value={searchTerm}
        onChange={handleSearch}
        className="search-input"
      />

      <table className="userlist-table">
        <thead>
          <tr>
            <th>Tên đăng nhập</th>
            <th>Họ tên</th>
            <th>Email</th>
            <th>Số điện thoại</th>
            <th>Vai trò</th>
            <th>Thao tác</th>
          </tr>
        </thead>
        <tbody>
          {filteredUsers.length > 0 ? (
            filteredUsers.map(({ userId, username, fullName, email, phone, role }) => (
              <tr key={userId} className="userlist-row">
                <td>{username}</td>
                <td>{fullName}</td>
                <td>{email}</td>
                <td>{phone}</td>
                <td className={`role-badge role-${role}`}>{role}</td>
                <td className="actions">
                  <button className="btn btn-edit" onClick={() => handleEdit(userId)}>Edit</button>
                  <button className="btn btn-delete" onClick={() => handleDelete(userId)}>Delete</button>
                </td>
              </tr>
            ))
          ) : (
            <tr>
              <td colSpan="6" className="no-users">Không có người dùng nào</td>
            </tr>
          )}
        </tbody>
      </table>

      {/* Form chỉnh sửa người dùng */}
      {editingUser && (
        <div className="modal">
          <form className="edit-form" onSubmit={handleEditSubmit}>
            <h3>Chỉnh sửa người dùng</h3>
            <label>
              Họ tên:
              <input type="text" name="fullName" value={editForm.fullName} onChange={handleEditChange} required />
            </label>
            <label>
              Email:
              <input type="email" name="email" value={editForm.email} onChange={handleEditChange} required />
            </label>
            <label>
              Số điện thoại:
              <input type="text" name="phone" value={editForm.phone} onChange={handleEditChange} required />
            </label>
            <label>
  Vai trò:
  <select name="role" value={editForm.role || ''} onChange={handleEditChange}>
    <option value="admin">Admin</option>
    <option value="accountant">Accountant</option>
    <option value="resident">Resident</option>
  </select>
</label>

            <label>
              Mật khẩu (mới):
              <input type="password" name="password" value={editForm.password} onChange={handleEditChange} />
            </label>
            <div className="form-actions">
              <button type="submit" className="btn btn-save">Lưu</button>
              <button type="button" className="btn btn-cancel" onClick={() => setEditingUser(null)}>Hủy</button>
            </div>
          </form>
        </div>
      )}
    </div>
  );
};

export default UserList;
