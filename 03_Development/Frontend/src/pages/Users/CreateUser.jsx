import React, { useState } from "react";
import axios from "axios";
import "./CreateUser.css";

const CreateUser = () => {
  const [formData, setFormData] = useState({
    username: "",
    fullName: "",
    email: "",
    phone: "",
    role: "resident",
    password: ""
  });

  const [message, setMessage] = useState("");

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await axios.post("http://localhost:8080/api/users", formData);
      setMessage("Tạo tài khoản thành công!");
      setFormData({
        username: "",
        fullName: "",
        email: "",
        phone: "",
        role: "resident",
        password: ""
      });
    } catch (error) {
      console.error("Lỗi khi tạo tài khoản:", error);
      setMessage("Tạo tài khoản thất bại.");
    }
  };

  return (
    <div className="create-user-container">
      <h2>Tạo tài khoản mới</h2>
      <form className="create-user-form" onSubmit={handleSubmit}>
        <label>Tên đăng nhập</label>
        <input
          type="text"
          name="username"
          value={formData.username}
          onChange={handleChange}
          required
        />

        <label>Họ và tên</label>
        <input
          type="text"
          name="fullName"
          value={formData.fullName}
          onChange={handleChange}
          required
        />

        <label>Email</label>
        <input
          type="email"
          name="email"
          value={formData.email}
          onChange={handleChange}
        />

        <label>Số điện thoại</label>
        <input
          type="text"
          name="phone"
          value={formData.phone}
          onChange={handleChange}
        />

        <label>Vai trò</label>
        <select name="role" value={formData.role} onChange={handleChange}>
          <option value="admin">Quản trị viên</option>
          <option value="accountant">Kế toán</option>
          <option value="resident">Cư dân</option>
        </select>

        <label>Mật khẩu</label>
        <input
          type="password"
          name="password"
          value={formData.password}
          onChange={handleChange}
          required
        />

        <button type="submit">Tạo tài khoản</button>
        {message && <p className="message">{message}</p>}
      </form>
    </div>
  );
};

export default CreateUser;
