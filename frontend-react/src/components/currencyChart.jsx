import React from "react";
import {
  AreaChart,
  Area,
  XAxis,
  YAxis,
  // CartesianGrid,
  Tooltip
} from "recharts";

function CurrencyChart(serie, serieMin, serieMax) {
  // console.log("CurrencyChart");
  // console.log(serieMin);
  // console.log(serieMax);
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