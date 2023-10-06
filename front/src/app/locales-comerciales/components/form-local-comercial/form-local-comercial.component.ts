import { Component, inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import Swal from 'sweetalert2';
import { LocalesComercialesService } from '../../services/locales-comerciales.service';
import { CategoryService } from '../../services/category.service';
import { Categoria } from '../../interfaces/Categoria';


@Component({
  selector: 'app-form-local-comercial',
  templateUrl: './form-local-comercial.component.html',
  styleUrls: ['./form-local-comercial.component.css'],
})

export class FormLocalComercialComponent {

  tiendaForm!: FormGroup;
  estados: string[] = ['ACTIVO', 'INACTIVO', 'EN_DEUDA', 'EN_DESALOJO'];
  categoriesData: Categoria[] = [];
  subcategorias: any = {};
  isLoading: boolean = false;
  mostrarFormulario: boolean = true;
  roles: { [key: string]: boolean } = {
      admin: false ,
      usuario_local: false ,
      vigilante: false
  };

  private localesComercialesService = inject(LocalesComercialesService);
  private categoryService = inject(CategoryService);

  constructor(private formBuilder: FormBuilder) {}

  ngOnInit() {
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

    this.createForm();
    this.loadCategories();
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
      Swal.fire('Error', 'Todos los campos deben ser llenos', 'error');
      return;
    }

    this.isLoading = true;

    const data = {
      localNombre: this.tiendaForm.value.nombreNegocio,
      localUbicacion: this.tiendaForm.value.ubicacion,
      localRepresentanteLegal: this.tiendaForm.value.representanteLegal,
      localCelular: this.tiendaForm.value.telefonoContacto,
      localEstado: this.tiendaForm.value.estado,
      localSubcategoriaId: this.tiendaForm.value.subCategoria,
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
