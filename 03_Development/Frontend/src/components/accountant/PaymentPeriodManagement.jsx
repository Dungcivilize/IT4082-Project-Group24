import React, { useState, useEffect } from 'react';
import { getAllPaymentPeriods, createPaymentPeriod } from '../../api/paymentPeriod';
import { Button, Form, Input, Table, message, Modal } from 'antd';
import './PaymentPeriodManagement.css';

const PaymentPeriodManagement = () => {
  const [paymentPeriods, setPaymentPeriods] = useState([]);
  const [loading, setLoading] = useState(false);
  const [isModalVisible, setIsModalVisible] = useState(false);
  const [form] = Form.useForm();

  const fetchPaymentPeriods = async () => {
    try {
      setLoading(true);
      const data = await getAllPaymentPeriods();
      setPaymentPeriods(data);
    } catch (error) {
      message.error('Không thể tải danh sách kỳ thu phí');
      console.error('Error:', error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchPaymentPeriods();
  }, []);

  const handleCreate = async (values) => {
    try {
      setLoading(true);
      console.log('Form values:', values); // Log để debug
      await createPaymentPeriod(values);
      message.success('Tạo kỳ thu phí thành công');
      setIsModalVisible(false);
      form.resetFields();
      fetchPaymentPeriods();
    } catch (error) {
      console.error('Error creating payment period:', error);
      message.error(error.response?.data || 'Không thể tạo kỳ thu phí');
    } finally {
      setLoading(false);
    }
  };

  const columns = [
    {
      title: 'ID',
      dataIndex: 'paymentPeriodId',
      key: 'paymentPeriodId',
    },
    {
      title: 'Tháng',
      dataIndex: 'month',
      key: 'month',
    },
    {
      title: 'Năm',
      dataIndex: 'year',
      key: 'year',
    },
    {
      title: 'Trạng thái',
      dataIndex: 'status',
      key: 'status',
      render: (status) => {
        const statusText = {
          'collecting': 'Đang thu phí',
          'completed': 'Đã hoàn thành'
        };
        return statusText[status] || status;
      }
    },
    {
      title: 'Ghi chú',
      dataIndex: 'note',
      key: 'note',
    }
  ];

  return (
    <div className="payment-period-management">
      <div className="header">
        <h2>Quản lý kỳ thu phí</h2>
        <Button type="primary" onClick={() => setIsModalVisible(true)}>
          Tạo kỳ thu phí mới
        </Button>
      </div>

      <Table
        columns={columns}
        dataSource={paymentPeriods}
        loading={loading}
        rowKey="paymentPeriodId"
      />

      <Modal
        title="Tạo kỳ thu phí mới"
        open={isModalVisible}
        onCancel={() => {
          setIsModalVisible(false);
          form.resetFields();
        }}
        footer={null}
        destroyOnHidden
      >
        <Form
          form={form}
          onFinish={handleCreate}
          layout="vertical"
          initialValues={{
            month: '',
            year: '',
            note: ''
          }}
        >
          <Form.Item
            name="month"
            label="Tháng"
            rules={[
              { required: true, message: 'Vui lòng nhập tháng' },
              { 
                validator: (_, value) => {
                  const month = parseInt(value);
                  if (isNaN(month) || month < 1 || month > 12) {
                    return Promise.reject('Tháng phải từ 1 đến 12');
                  }
                  return Promise.resolve();
                }
              }
            ]}
          >
            <Input type="number" min={1} max={12} placeholder="Nhập tháng (1-12)" />
          </Form.Item>

          <Form.Item
            name="year"
            label="Năm"
            rules={[
              { required: true, message: 'Vui lòng nhập năm' },
              {
                validator: (_, value) => {
                  const year = parseInt(value);
                  if (isNaN(year) || year < 2000) {
                    return Promise.reject('Năm phải từ 2000 trở lên');
                  }
                  return Promise.resolve();
                }
              }
            ]}
          >
            <Input type="number" min={2000} placeholder="Nhập năm" />
          </Form.Item>

          <Form.Item
            name="note"
            label="Ghi chú"
          >
            <Input.TextArea placeholder="Nhập ghi chú (nếu có)" />
          </Form.Item>

          <Form.Item>
            <Button type="primary" htmlType="submit" loading={loading}>
              Tạo mới
            </Button>
          </Form.Item>
        </Form>
      </Modal>
    </div>
  );
};

export default PaymentPeriodManagement; 