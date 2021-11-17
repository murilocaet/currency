import React from "react";
import {
  AreaChart,
  Area,
  XAxis,
  YAxis,
  // CartesianGrid,
  Tooltip
} from "recharts";

// const data = [
//   {
//     date: "Sep 1",
//     value: 1.18480
//   },
//   {
//     date: "Sep 2",
//     value: 1.18620
//   },
//   {
//     date: "Sep 5",
//     value: 1.18615
//   },
//   {
//     date: "Sep 6",
//     value: 1.18207
//   },
//   {
//     date: "Sep 7",
//     value: 1.17930
//   },
//   {
//     date: "Sep 8",
//     value: 1.18444
//   },
//   {
//     date: "Sep 9",
//     value: 1.18746
//   },
// ];

function CurrencyChart(serie, serieMin, serieMax) {
    return (
        <AreaChart
            width={500}
            height={200}
            data={serie.data}
            margin={{
              top: 30,
              right: 30,
              left: 0,
              bottom: 0
            }}
        >
            {/* <CartesianGrid strokeDasharray="5 5" /> */}
            <XAxis dataKey="name" />
            <YAxis type="number" domain={[serieMin, serieMax]}/>
            <Tooltip />
            <Area type="monotone" dataKey="value" stroke="#e74b2b" fill="#f6d0da" />
        </AreaChart>
    );
}

export default CurrencyChart;