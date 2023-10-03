import { Component, inject } from '@angular/core';
import { LocalesComercialesService } from '../../services/locales-comerciales.service';
import Swal from 'sweetalert2';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import * as pdfMake from 'pdfmake/build/pdfmake';
import * as pdfFonts from 'pdfmake/build/vfs_fonts';
import { CategoryService } from '../../services/category.service';

(<any>pdfMake).vfs = pdfFonts.pdfMake.vfs;

@Component({
  selector: 'app-viewlocales',
  templateUrl: './viewlocales.component.html',
  styleUrls: ['./viewlocales.component.css'],
})
export class ViewlocalesComponent {
  shops: any[] = [];
  selectedShop: any = {};
  updateForm!: FormGroup;
  categoriesData: string[] = [];
  estados: string[] = ['ACTIVO', 'INACTIVO', 'EN_DEUDA', 'EN_DESALOJO'];
  subcategorias: any = {};

  private categoryService = inject(CategoryService);

  constructor(
    private localesComercialesService: LocalesComercialesService,
    private formBuilder: FormBuilder
  ) {
    this.localesComercialesService = localesComercialesService;
    this.updateForm = this.formBuilder.group({
      nombreNegocio: ['', Validators.required],
      ubicacion: ['', Validators.required],
      representanteLegal: ['', Validators.required],
      telefonoContacto: ['', Validators.required],
      estado: ['', Validators.required],
      categoria: ['', Validators.required],
      subCategoria: [ this.selectedShop.subCategoria?.id || '', Validators.required],
    });
  }

  ngOnInit(): void {
    this.loadLocalesComerciales();
    this.loadCategories();
    this.localesComercialesService.getShops().subscribe(
      (data: any[]) => {
        this.shops = data;
      },
      (error) => {
        console.error(error);
      }
    );
  }

  private loadCategories(): void {
    this.categoryService.loadCategories().subscribe(
      (response) => {
        response.forEach((item) => {
          const categoria = Object.keys(item)[0];
          const categoriaId = item[categoria].id;
          const subCategorias = item[categoria].subCategories.map((subCat) => {
            return { id: subCat.id, nombre: subCat.nombre };
          });

          this.categoriesData.push(categoria);
          this.subcategorias[subCategorias] = subCategorias;
        });
      },
      (error) => {
        console.error('Error loading categories:', error);
      }
    );
  }

  onCategoriaChange() {
    const categoria = this.updateForm.get('categoria')!.value;
    this.updateForm.get('subCategoria')!.setValue('');
    this.updateForm.get('subCategoria')!.markAsUntouched();
    if (categoria && this.subcategorias[categoria]) {
      this.updateForm.get('subCategoria')!.setValidators(Validators.required);
    } else {
      this.updateForm.get('subCategoria')!.clearValidators();
    }
    this.updateForm.get('subCategoria')!.updateValueAndValidity();
  }

  createPDF() {
    const pdfDefinition: any = {
      content: [
        {
          text: 'Datos de los locales comerciales',
          style: 'header',
        },
        {
          table: {
            headerRows: 1,
            widths: ['auto', 'auto', 'auto', 'auto', 'auto', 'auto'], // Especifica anchos personalizados aquí
            body: [
              [
                'Nombre Negocio',
                'Ubicación',
                'Representante Legal',
                'Teléfono de Contacto',
                'Estado',
                'Categoría',
              ],
  
              ...this.shops.map((shop) => [
                shop.nombreNegocio,
                shop.ubicacion,
                shop.representanteLegal,
                shop.telefonoContacto,
                shop.estado,
                shop.subCategoria.categoria.nombre,
              ]),
            ],
          },
        },
      ],
      styles: {
        header: {
          fontSize: 18,
          bold: true,
          margin: [0, 0, 0, 10],
        },
      },
    };
  
    const pdf = pdfMake.createPdf(pdfDefinition);
    pdf.open();
  }

  private loadLocalesComerciales(): void {
    this.localesComercialesService.loadShops();
  }

  handleModal(shop: any) {
    this.selectedShop = { ...shop };

    console.log(this.selectedShop)

    this.updateForm.patchValue({
      nombreNegocio: this.selectedShop.nombreNegocio,
      ubicacion: this.selectedShop.ubicacion,
      representanteLegal: this.selectedShop.representanteLegal,
      telefonoContacto: this.selectedShop.telefonoContacto,
      estado: this.selectedShop.estado,
      categoria: this.selectedShop.categoria,
      subcategoria: this.selectedShop.subCategoria.id,
    });
  }

  onEditShop(id: any) {
    if (!this.updateForm.valid) {
      // Muestra un mensaje de error utilizando SweetAlert2
      Swal.fire({
        icon: 'error',
        title: 'Error',
        text: 'Por favor, completa todos los campos correctamente.',
        confirmButtonText: 'Cerrar',
      });
      return; // Detiene la ejecución del método
    }

    // Si los campos son válidos, continúa con el proceso de actualización
    const data = {
      nombreNegocio: this.updateForm.value.nombreNegocio,
      ubicacion: this.updateForm.value.ubicacion,
      representanteLegal: this.updateForm.value.representanteLegal,
      telefonoContacto: this.updateForm.value.telefonoContacto,
      estado: this.updateForm.value.estado,
      subCategoria: {
        id: parseInt(this.updateForm.value.subCategoria),
      },
    };

    this.localesComercialesService.updateShop(id, data).subscribe(
      () => {
        Swal.fire({
          icon: 'success',
          title: 'Éxito',
          text: 'El shop se actualizó correctamente.',
          showConfirmButton: false,
          timer: 2000,
        });
      },
      (error) => {
        Swal.fire({
          icon: 'error',
          title: 'Error',
          text: 'No se pudo actualizar el shop.',
          confirmButtonText: 'Cerrar',
        });
        console.error(error);
      }
    );
  }

  onDelete(id: any) {
    this.localesComercialesService.deleteShop(id).subscribe(
      () => {
        Swal.fire({
          icon: 'success',
          title: 'Éxito',
          text: 'El objeto se eliminó correctamente.',
          showConfirmButton: false,
          timer: 2000,
        });
      },
      (error) => {
        Swal.fire({
          icon: 'error',
          title: 'Error',
          text: 'No se pudo eliminar correctamente.',
          showConfirmButton: false,
          timer: 2000,
        });
      }
    );
  }
  onFormSubmit() {}
}