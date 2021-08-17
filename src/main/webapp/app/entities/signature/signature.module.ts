import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SignatureComponent } from './list/signature.component';
import { SignatureDetailComponent } from './detail/signature-detail.component';
import { SignatureUpdateComponent } from './update/signature-update.component';
import { SignatureDeleteDialogComponent } from './delete/signature-delete-dialog.component';
import { SignatureRoutingModule } from './route/signature-routing.module';

@NgModule({
  imports: [SharedModule, SignatureRoutingModule],
  declarations: [SignatureComponent, SignatureDetailComponent, SignatureUpdateComponent, SignatureDeleteDialogComponent],
  entryComponents: [SignatureDeleteDialogComponent],
})
export class SignatureModule {}
