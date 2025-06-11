import React, { useState } from 'react';
import { Link, useLocation } from 'react-router-dom';
import styles from './Sidebar.module.css';

const Sidebar = () => {
  const location = useLocation();

  const menu = [
    { label: 'Thống kê', path: '/admin', icon: '📊' },
    {
      label: 'Tài khoản',
      icon: '👤',
      children: [
        { label: 'Quản lý tài khoản', path: '/admin/users/list' },
        { label: 'Thêm tài khoản', path: '/admin/users/add' },
      ],
    },
    {
      label: 'Cư dân',
      icon: '🏠',
      children: [
        { label: 'Danh sách cư dân', path: '/admin/residents' },
        { label: 'Chủ hộ & thành viên', path: '/admin/households' },
        { label: 'Tạm trú / Tạm vắng', path: '/admin/temporary' },
        { label: 'Phương tiện', path: '/admin/vehicles' },
      ],
    },
  ];

  const [openMenus, setOpenMenus] = useState({});
  const [collapsed, setCollapsed] = useState(false);

  const toggleMenu = (label) => {
    setOpenMenus((prev) => ({
      ...prev,
      [label]: !prev[label],
    }));
  };

  return (
    <aside
      className={`${styles.sidebar} ${collapsed ? styles.collapsed : ''}`}
      onMouseEnter={() => setCollapsed(false)}
      onMouseLeave={() => setCollapsed(true)}
    >
      <div className={styles.sidebarHeader}>{collapsed ? 'TA' : 'TumAdmin'}</div>
      <nav className={styles.nav}>
        {menu.map((item) => {
          const isOpen = openMenus[item.label];
          const hasChildren = !!item.children;
          const activeChild = hasChildren && item.children.some(c => c.path === location.pathname);
          const activeParent = !hasChildren && item.path === location.pathname;

          return (
            <div key={item.label} className={styles.menuSection}>
              <p
                className={`${styles.menuSectionLabel} ${
                  (activeChild || activeParent) ? styles.activeLabel : ''
                }`}
                onClick={() => hasChildren && toggleMenu(item.label)}
                role={hasChildren ? 'button' : undefined}
                tabIndex={0}
                onKeyDown={e => { if (e.key === 'Enter') toggleMenu(item.label); }}
              >
                <span className={styles.icon}>{item.icon}</span>
                {!collapsed && item.label}
                {hasChildren && !collapsed && (
                  <span className={styles.caret}>{isOpen ? '▼' : '▶'}</span>
                )}
              </p>

              {hasChildren && isOpen && !collapsed && (
                <ul className={styles.submenu}>
                  {item.children.map((child) => {
                    const isActive = location.pathname === child.path;
                    return (
                      <li key={child.path}>
                        <Link
                          to={child.path}
                          className={`${styles.submenuLink} ${
                            isActive ? styles.activeSubmenuLink : ''
                          }`}
                        >
                          {child.label}
                        </Link>
                      </li>
                    );
                  })}
                </ul>
              )}
            </div>
          );
        })}
      </nav>
    </aside>
  );
};

export default Sidebar;
