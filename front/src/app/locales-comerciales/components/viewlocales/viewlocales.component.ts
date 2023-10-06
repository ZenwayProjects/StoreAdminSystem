import { Component, inject } from '@angular/core';
import { LocalesComercialesService } from '../../services/locales-comerciales.service';
import Swal from 'sweetalert2';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import * as pdfMake from 'pdfmake/build/pdfmake';
import * as pdfFonts from 'pdfmake/build/vfs_fonts';
import { CategoryService } from '../../services/category.service';
import {Categoria} from "../../interfaces/Categoria";

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
  categoriesData: Categoria[] = [];
  estados: string[] = ['ACTIVO', 'INACTIVO', 'EN_DEUDA', 'EN_DESALOJO'];
  subcategorias: any = {};
  roles: { [key: string]: boolean } = {
    admin: false ,
    usuario_local: false ,
    vigilante: false
  };

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
    const rolesStr = localStorage.getItem('roles');
    if (rolesStr) {
        if (rolesStr.includes('ADMIN')) {
          console.log("es admin");
          this.roles['admin'] = true;
        }
        if (rolesStr.includes('VIGILANTE')) {
          console.log("es vigilante")
          this.roles['vigilante'] = true;
        }
        if (rolesStr.includes('USUARIO_LOCAL')) {
          console.log("es usuario_local")
          this.roles['usuario_local'] = true;
        }
    }

    this.loadLocalesComerciales();
    this.loadCategories();
    this.localesComercialesService.getShops().subscribe(
      (data: any[]) => {
        console.log(data)
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
          let categoria : Categoria;
          categoria = item;
          const categoriaId = item.categoriaId;
          const subCategorias = item.subcategorias.map((subCat) => {
            return { id: subCat.id, nombre: subCat.subcNombre };
          });
          this.categoriesData.push(categoria);
          this.subcategorias[categoriaId] = subCategorias;
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
    this.shops.map(shop => console.log(shop))
    console.log();
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
                shop.localNombre,
                shop.localUbicacion,
                shop.localRepresentanteLegal,
                shop.localCelular,
                shop.localEstado,
                shop.localSubcategoria.subcNombre,
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
    if(this.roles['usuario_local'])
      this.loadLocalesComercialesUsuario();
  }

  private loadLocalesComercialesUsuario(): void {
    console.log(this.shops)
    this.localesComercialesService.loadShopsUsuarioLocal();
  }

  handleModal(shop: any) {
    this.selectedShop = { ...shop };

    console.log(this.selectedShop)

    this.updateForm.patchValue({
      nombreNegocio: this.selectedShop.localNombre,
      ubicacion: this.selectedShop.localUbicacion,
      representanteLegal: this.selectedShop.localRepresentanteLegal,
      telefonoContacto: this.selectedShop.localCelular,
      estado: this.selectedShop.localEstado,
      categoria: this.selectedShop.categoria,
      subcategoria: this.selectedShop.localSubcategoria,
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
      localNombre: this.updateForm.value.nombreNegocio,
      localUbicacion: this.updateForm.value.ubicacion,
      localRepresentanteLegal: this.updateForm.value.representanteLegal,
      localCelular: this.updateForm.value.telefonoContacto,
      localEstado: this.updateForm.value.estado,
      localSubcategoria: {
        id: parseInt(this.updateForm.value.subCategoria),
      },
    };
    console.log(id)
    this.localesComercialesService.updateShop(id, data).subscribe(
      () => {
        Swal.fire({
          icon: 'success',
          title: 'Éxito',
          text: 'El shop se actualizó correctamente.',
          showConfirmButton: false,
          timer: 2000,
        });
        window.location.reload();
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