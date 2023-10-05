import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { UsersService } from '../../services/users.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { LocalesComercialesService } from '../../services/locales-comerciales.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css'],
})
export class DashboardComponent {
  userForm!: FormGroup;
  locales: string[] = ['Local 1', 'Local 2', 'Local 3'];
  shops: any[] = [];
  roles: { [key: string]: boolean } = {
    admin: false ,
    usuario_local: false ,
    vigilante: false
  };

  constructor(
    private authService: AuthService,
    private formBuilder: FormBuilder,
    private userService: UsersService,
    private localesComercialesService: LocalesComercialesService
  ) {
    this.initializeForm();
    this.localesComercialesService = localesComercialesService;
  }

  private router = inject(Router);

  initializeForm() {
    this.userForm = this.formBuilder.group({
      nombre: ['', Validators.required],
      documento: ['', Validators.required],
      apellido: ['', Validators.required],
      username: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required]],
      selectedLocale: ['', Validators.required],
    });
  }

  ngOnInit(): void {
    const rolesStr = localStorage.getItem('roles');
    if (rolesStr) {
      try {
        const roles = JSON.parse(rolesStr);
        if (roles.includes('ADMIN')) {
          console.log("es admin");
          this.roles['admin'] = true;
        }
        if (roles.includes('VIGILANTE')) {
          console.log("es vigilante")
          this.roles['vigilante'] = true;
        }
        if (roles.includes('USUARIO_LOCAL')) {
          console.log("es usuario_local")
          this.roles['usuario_local'] = true;
        }
      } catch (error) {
        console.error('Error al analizar la cadena JSON de roles:', error);
      }
    }


    this.localesComercialesService.getShops().subscribe(
      (data: any[]) => {
        this.shops = data;
        console.log(this.shops);
      },
      (error) => {
        console.error(error);
      }
    );
  }

  onSubmit() {
    if (!this.userForm.valid) {
      Swal.fire({
        icon: 'error',
        title: 'Error',
        text: 'Por favor, complete todos los campos correctamente.',
      });
      return;
    }

    const { nombre, apellido, documento, username, email, password, selectedLocale } = this.userForm.value;

    const updatedUserFormValue = {
      nombre: nombre,
      numeroDocumento: documento,
      apellido: apellido,
      email: email,
      login: username,
      password: password,
      local: selectedLocale
    };

    this.userService.addUser(updatedUserFormValue).subscribe({
      next: (response) => {
        console.log(response);
        Swal.fire('Éxito', 'Guardado exitosamente', 'success');
        this.userForm.reset();

      },
      error: (error) => {
        console.log(error);
        Swal.fire('Error', 'Error al guardar', 'error');
      }
    });
  }

  onLogout() {
    this.authService.logout();
    this.router.navigateByUrl('login');
    this.shops = [];
    this.localesComercialesService.clearShops();
  }
}
