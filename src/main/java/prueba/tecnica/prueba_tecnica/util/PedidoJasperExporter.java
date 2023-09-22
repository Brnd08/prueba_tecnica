package prueba.tecnica.prueba_tecnica.util;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PedidoJasperExporter {
    private static final Logger logger = LogManager.getLogger(PedidoJasperExporter.class);
    private static final String URI_REPORTE_PLANTILLA = "classpath:reporte.jrxml";
    public static byte[] exportToPDF(PedidoJasper pedidoJasper) throws JRException {
        logger.log(Level.INFO, "Exportando pedido. pedidoJasper {}", pedidoJasper);
        JasperReport reporteJasper = cargarJasperReport(URI_REPORTE_PLANTILLA);
        JasperPrint reporteRellenado = rellenarReporte(pedidoJasper, reporteJasper);
        return JasperExportManager.exportReportToPdf(reporteRellenado);
    }

    public static JasperPrint rellenarReporte(PedidoJasper pedidoJasper, JasperReport reporteJasper) throws JRException {

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("articulosPedido", new JRBeanCollectionDataSource(pedidoJasper.getArticulosPedido()));
        parameters.put("idPedido", pedidoJasper.getIdPedido());
        parameters.put("descripcionPedido", pedidoJasper.getDescripcionPedido());
        parameters.put("clientePedido", pedidoJasper.getClientePedido());

        return JasperFillManager.fillReport(reporteJasper, parameters, new JREmptyDataSource());
    }

    static JasperReport cargarJasperReport(String uriPlantilla) {
        try {
            final File plantillaJasper = ResourceUtils.getFile(uriPlantilla);
            FileInputStream inputStream = new FileInputStream(plantillaJasper);
            JasperReport report = JasperCompileManager.compileReport(inputStream);
            inputStream.close();
            return report;
        } catch (Exception e) {
            logger.log(Level.FATAL, "No se pudo cargar la plantilla para JasperReport. URI plantilla: {}, Error: {}", uriPlantilla, e.getMessage());
            throw new RuntimeException(e);
        }
    }

}
