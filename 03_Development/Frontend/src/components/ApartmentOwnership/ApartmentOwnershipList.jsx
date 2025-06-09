import React, { useState, useEffect } from 'react';
import { Table, Tag, Space, Button, Modal, Form, DatePicker, Select, message } from 'antd';
import moment from 'moment';
import apartmentOwnershipApi from '../../api/apartmentOwnershipApi';
import userApi from '../../api/userApi';
import apartmentApi from '../../api/apartmentApi';

const ApartmentOwnershipList = () => {
    const [ownerships, setOwnerships] = useState([]);
    const [loading, setLoading] = useState(false);
    const [users, setUsers] = useState([]);
    const [apartments, setApartments] = useState([]);
    const [isModalVisible, setIsModalVisible] = useState(false);
    const [form] = Form.useForm();

    useEffect(() => {
        fetchOwnerships();
        fetchUsers();
        fetchApartments();
    }, []);

    const fetchOwnerships = async () => {
        try {
            setLoading(true);
            const response = await apartmentOwnershipApi.getAll();
            setOwnerships(response.data);
        } catch (error) {
            message.error('Không thể tải danh sách quyền sở hữu');
        } finally {
            setLoading(false);
        }
    };

    const fetchUsers = async () => {
        try {
            const response = await userApi.getAll();
            setUsers(response.data);
        } catch (error) {
            message.error('Không thể tải danh sách người dùng');
        }
    };

    const fetchApartments = async () => {
        try {
            const response = await apartmentApi.getAll();
            setApartments(response.data);
        } catch (error) {
            message.error('Không thể tải danh sách căn hộ');
        }
    };

    const handleCreate = () => {
        setIsModalVisible(true);
    };

    const handleCancel = () => {
        setIsModalVisible(false);
        form.resetFields();
    };

    const handleSubmit = async (values) => {
        try {
            await apartmentOwnershipApi.create({
                ...values,
                startDate: values.startDate.format('YYYY-MM-DD')
            });
            message.success('Tạo quyền sở hữu thành công');
            fetchOwnerships();
            handleCancel();
        } catch (error) {
            message.error('Không thể tạo quyền sở hữu');
        }
    };

    const handleEndOwnership = async (record) => {
        try {
            await apartmentOwnershipApi.end(record.ownershipId, {
                endDate: moment().format('YYYY-MM-DD')
            });
            message.success('Kết thúc quyền sở hữu thành công');
            fetchOwnerships();
        } catch (error) {
            message.error('Không thể kết thúc quyền sở hữu');
        }
    };

    const columns = [
        {
            title: 'Mã căn hộ',
            dataIndex: ['apartment', 'apartmentCode'],
            key: 'apartmentCode'
        },
        {
            title: 'Chủ sở hữu',
            dataIndex: ['user', 'fullName'],
            key: 'fullName'
        },
        {
            title: 'Ngày bắt đầu',
            dataIndex: 'startDate',
            key: 'startDate',
            render: (date) => moment(date).format('DD/MM/YYYY')
        },
        {
            title: 'Ngày kết thúc',
            dataIndex: 'endDate',
            key: 'endDate',
            render: (date) => date ? moment(date).format('DD/MM/YYYY') : '-'
        },
        {
            title: 'Trạng thái',
            dataIndex: 'status',
            key: 'status',
            render: (status) => (
                <Tag color={status === 'active' ? 'green' : 'red'}>
                    {status === 'active' ? 'Đang sở hữu' : 'Đã kết thúc'}
                </Tag>
            )
        },
        {
            title: 'Thao tác',
            key: 'action',
            render: (_, record) => (
                <Space>
                    {record.status === 'active' && (
                        <Button type="primary" danger onClick={() => handleEndOwnership(record)}>
                            Kết thúc
                        </Button>
                    )}
                </Space>
            )
        }
    ];

    return (
        <div>
            <div style={{ marginBottom: 16 }}>
                <Button type="primary" onClick={handleCreate}>
                    Tạo mới
                </Button>
            </div>

            <Table
                columns={columns}
                dataSource={ownerships}
                loading={loading}
                rowKey="ownershipId"
            />

            <Modal
                title="Tạo quyền sở hữu mới"
                visible={isModalVisible}
                onCancel={handleCancel}
                onOk={form.submit}
            >
                <Form form={form} onFinish={handleSubmit}>
                    <Form.Item
                        name="apartmentId"
                        label="Căn hộ"
                        rules={[{ required: true, message: 'Vui lòng chọn căn hộ' }]}
                    >
                        <Select>
                            {apartments.map(apartment => (
                                <Select.Option key={apartment.apartmentId} value={apartment.apartmentId}>
                                    {apartment.apartmentCode}
                                </Select.Option>
                            ))}
                        </Select>
                    </Form.Item>

                    <Form.Item
                        name="userId"
                        label="Chủ sở hữu"
                        rules={[{ required: true, message: 'Vui lòng chọn chủ sở hữu' }]}
                    >
                        <Select>
                            {users.map(user => (
                                <Select.Option key={user.userId} value={user.userId}>
                                    {user.fullName}
                                </Select.Option>
                            ))}
                        </Select>
                    </Form.Item>

                    <Form.Item
                        name="startDate"
                        label="Ngày bắt đầu"
                        rules={[{ required: true, message: 'Vui lòng chọn ngày bắt đầu' }]}
                    >
                        <DatePicker format="DD/MM/YYYY" />
                    </Form.Item>
                </Form>
            </Modal>
        </div>
    );
};

export default ApartmentOwnershipList; 