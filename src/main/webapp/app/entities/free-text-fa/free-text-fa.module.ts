import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { FreeTextFaComponent } from './list/free-text-fa.component';
import { FreeTextFaDetailComponent } from './detail/free-text-fa-detail.component';
import { FreeTextFaUpdateComponent } from './update/free-text-fa-update.component';
import { FreeTextFaDeleteDialogComponent } from './delete/free-text-fa-delete-dialog.component';
import { FreeTextFaRoutingModule } from './route/free-text-fa-routing.module';

@NgModule({
  imports: [SharedModule, FreeTextFaRoutingModule],
  declarations: [FreeTextFaComponent, FreeTextFaDetailComponent, FreeTextFaUpdateComponent, FreeTextFaDeleteDialogComponent],
  entryComponents: [FreeTextFaDeleteDialogComponent],
})
export class FreeTextFaModule {}
