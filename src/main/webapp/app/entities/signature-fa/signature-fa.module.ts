import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SignatureFaComponent } from './list/signature-fa.component';
import { SignatureFaDetailComponent } from './detail/signature-fa-detail.component';
import { SignatureFaUpdateComponent } from './update/signature-fa-update.component';
import { SignatureFaDeleteDialogComponent } from './delete/signature-fa-delete-dialog.component';
import { SignatureFaRoutingModule } from './route/signature-fa-routing.module';

@NgModule({
  imports: [SharedModule, SignatureFaRoutingModule],
  declarations: [SignatureFaComponent, SignatureFaDetailComponent, SignatureFaUpdateComponent, SignatureFaDeleteDialogComponent],
  entryComponents: [SignatureFaDeleteDialogComponent],
})
export class SignatureFaModule {}
