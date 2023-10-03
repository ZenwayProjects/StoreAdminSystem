import { Component, inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import Swal from 'sweetalert2';
import { LocalesComercialesService } from '../../services/locales-comerciales.service';
import { CategoryService } from '../../services/category.service';

@Component({
  selector: 'app-form-local-comercial',
  templateUrl: './form-local-comercial.component.html',
  styleUrls: ['./form-local-comercial.component.css'],
})
export class FormLocalComercialComponent {
  tiendaForm!: FormGroup;
  estados: string[] = ['ACTIVO', 'INACTIVO', 'EN_DEUDA', 'EN_DESALOJO'];
  categoriesData: string[] = [];
  subcategorias: any = {};
  isLoading: boolean = false;

  private localesComercialesService = inject(LocalesComercialesService);
  private categoryService = inject(CategoryService);

  constructor(private formBuilder: FormBuilder) {}

  ngOnInit() {
    this.createForm();
    this.loadCategories();
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
          this.subcategorias[categoria] = subCategorias;
        });
      },
      (error) => {
        console.error('Error loading categories:', error);
      }
    );
  }

  onCategoriaChange() {
    const categoria = this.tiendaForm.get('categoria')!.value;
    this.tiendaForm.get('subcategoria')!.setValue('');
    this.tiendaForm.get('subcategoria')!.markAsUntouched();
    if (categoria && this.subcategorias[categoria]) {
      this.tiendaForm.get('subcategoria')!.setValidators(Validators.required);
    } else {
      this.tiendaForm.get('subcategoria')!.clearValidators();
    }
    this.tiendaForm.get('subcategoria')!.updateValueAndValidity();
  }

  createForm() {
    this.tiendaForm = this.formBuilder.group({
      nombreNegocio: ['', Validators.required],
      ubicacion: ['', Validators.required],
      representanteLegal: ['', Validators.required],
      telefonoContacto: ['', Validators.required],
      estado: ['', Validators.required],
      categoria: ['', Validators.required],
      subCategoria: ['', Validators.required],
    });
  }

  onSubmit() {
    if (!this.tiendaForm.valid) {
      Swal.fire('Error', 'Todos los campos deben ser llenados', 'error');
      return;
    }

    this.isLoading = true;

    const data = {
      nombreNegocio: this.tiendaForm.value.nombreNegocio,
      ubicacion: this.tiendaForm.value.ubicacion,
      representanteLegal: this.tiendaForm.value.representanteLegal,
      telefonoContacto: this.tiendaForm.value.telefonoContacto,
      estado: this.tiendaForm.value.estado,
      subCategoria: {
        id: parseInt(this.tiendaForm.value.subCategoria),
      },
    };

    this.localesComercialesService.addShop(data).subscribe(
      (response) => {
        Swal.fire('Éxito', 'Guardado exitosamente', 'success');
        this.tiendaForm.reset();
        this.localesComercialesService.loadShops();
        this.isLoading = false;
      },
      (error) => {
        Swal.fire('Error', 'Error al guardar', 'error');
        this.isLoading = false;
      }
    );
  }
}
