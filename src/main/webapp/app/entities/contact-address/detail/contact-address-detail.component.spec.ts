import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ContactAddressDetailComponent } from './contact-address-detail.component';

describe('Component Tests', () => {
  describe('ContactAddress Management Detail Component', () => {
    let comp: ContactAddressDetailComponent;
    let fixture: ComponentFixture<ContactAddressDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ContactAddressDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ contactAddress: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ContactAddressDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ContactAddressDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load contactAddress on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.contactAddress).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
