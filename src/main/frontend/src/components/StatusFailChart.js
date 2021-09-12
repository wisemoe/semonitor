import React from 'react';
import { Line } from '@ant-design/charts';

const StatusFailChart: React.FC = (props) => {
  const config = {
    data: props.data,
    xField: 'time',
    yField: 'status',
    label: {
      style: {
        fill: '#aaa',
      },
    },
  };

  let chart;

  return (
    <div>
      <Line {...config} onReady={(chartInstance) => (chart = chartInstance)} />
    </div>
  );
};
export default StatusFailChart;
