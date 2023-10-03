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
      username: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required]],
      selectedLocale: ['', Validators.required],
    });
  }

  ngOnInit(): void {
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

    const { username, email, password, selectedLocale } = this.userForm.value;

    const updatedUserFormValue = {
      email: email,
      username: username,
      password: password,
      shopModel: {
        id: selectedLocale,
      },
    };

    this.userService.addUser(updatedUserFormValue).subscribe(
      (response) => {
        Swal.fire('Éxito', 'Guardado exitosamente', 'success');
        this.userForm.reset();
      },
      (error) => {
        Swal.fire('Error', 'Error al guardar', 'error');
      }
    );
  }

  onLogout() {
    this.authService.logout();
    this.router.navigateByUrl('login');
    this.shops = [];
    this.localesComercialesService.clearShops();
  }
}
