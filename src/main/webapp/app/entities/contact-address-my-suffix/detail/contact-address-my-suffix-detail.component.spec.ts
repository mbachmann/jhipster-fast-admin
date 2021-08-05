import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ContactAddressMySuffixDetailComponent } from './contact-address-my-suffix-detail.component';

describe('Component Tests', () => {
  describe('ContactAddressMySuffix Management Detail Component', () => {
    let comp: ContactAddressMySuffixDetailComponent;
    let fixture: ComponentFixture<ContactAddressMySuffixDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ContactAddressMySuffixDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ contactAddress: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ContactAddressMySuffixDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ContactAddressMySuffixDetailComponent);
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
