package ObjetosBD;

import java.sql.ResultSet;

/**
 * Created by Franco on 29/4/2017.
 */

public class ResultBaseDeDatos {

    private boolean conecto;
    private ResultSet resultSet;

    public Boolean Conecto() {
        return conecto;
    }

    public void setConecto(Boolean conecto) {
        this.conecto = conecto;
    }

    public ResultSet getResultSet() {
        return resultSet;
    }

    public void setResultSet(ResultSet resultSet) {
        this.resultSet = resultSet;
    }
}
