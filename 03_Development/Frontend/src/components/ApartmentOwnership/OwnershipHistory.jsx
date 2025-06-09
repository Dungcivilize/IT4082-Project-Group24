import React, { useState, useEffect } from 'react';
import { Table, Timeline, Card, Spin, Empty } from 'antd';
import moment from 'moment';
import apartmentOwnershipApi from '../../api/apartmentOwnershipApi';

const OwnershipHistory = ({ apartmentId }) => {
    const [history, setHistory] = useState([]);
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        if (apartmentId) {
            fetchHistory();
        }
    }, [apartmentId]);

    const fetchHistory = async () => {
        try {
            setLoading(true);
            const response = await apartmentOwnershipApi.getByApartment(apartmentId);
            setHistory(response.data);
        } catch (error) {
            console.error('Error fetching ownership history:', error);
        } finally {
            setLoading(false);
        }
    };

    const columns = [
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
            render: (date) => date ? moment(date).format('DD/MM/YYYY') : 'Hiện tại'
        },
        {
            title: 'Thời gian sở hữu',
            key: 'duration',
            render: (_, record) => {
                const start = moment(record.startDate);
                const end = record.endDate ? moment(record.endDate) : moment();
                const duration = moment.duration(end.diff(start));
                const years = duration.years();
                const months = duration.months();
                const days = duration.days();

                return `${years > 0 ? `${years} năm ` : ''}${months > 0 ? `${months} tháng ` : ''}${days > 0 ? `${days} ngày` : ''}`;
            }
        }
    ];

    const timelineItems = history.map(record => ({
        color: record.endDate ? 'gray' : 'green',
        children: (
            <div>
                <p><strong>{record.user.fullName}</strong></p>
                <p>{moment(record.startDate).format('DD/MM/YYYY')} - {record.endDate ? moment(record.endDate).format('DD/MM/YYYY') : 'Hiện tại'}</p>
            </div>
        )
    }));

    if (loading) {
        return <Spin />;
    }

    if (!history.length) {
        return <Empty description="Chưa có lịch sử sở hữu" />;
    }

    return (
        <div>
            <Card title="Lịch sử sở hữu" style={{ marginBottom: 16 }}>
                <Timeline
                    mode="left"
                    items={timelineItems}
                />
            </Card>

            <Table
                columns={columns}
                dataSource={history}
                rowKey="ownershipId"
                pagination={false}
            />
        </div>
    );
};

export default OwnershipHistory; 