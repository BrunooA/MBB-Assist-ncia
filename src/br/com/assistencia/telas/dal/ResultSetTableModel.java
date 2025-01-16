/*Essa classe é usada para criar um modelo de tabela para exibir dados de um 
* ResultSet (conjunto de resultados de uma consulta SQL) em um componente de 
* tabela, como o JTable.*/
package br.com.assistencia.telas.dal;

import javax.swing.table.AbstractTableModel;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ResultSetTableModel extends AbstractTableModel {

    private final List<Object[]> data;
    private final String[] columnNames;

    public ResultSetTableModel(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        // Definir nomes das colunas
        columnNames = new String[columnCount];
        for (int i = 1; i <= columnCount; i++) {
            columnNames[i - 1] = metaData.getColumnName(i);
        }

        // Preencher dados
        data = new ArrayList<>();
        while (rs.next()) {
            Object[] row = new Object[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                row[i - 1] = rs.getObject(i);
            }
            data.add(row);
        }
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Object[] row = data.get(rowIndex);
        return row[columnIndex];
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
}