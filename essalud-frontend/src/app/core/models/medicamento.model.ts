export interface Medicamento {
  medicamentoId: number;
  nombre: string;
  presentacion: string;
  descripcion: string;
  codigoPetitorio: string | null;
  precioReferencial: number | null;
}