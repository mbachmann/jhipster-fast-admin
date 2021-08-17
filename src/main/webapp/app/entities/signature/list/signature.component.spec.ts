import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { SignatureService } from '../service/signature.service';

import { SignatureComponent } from './signature.component';

describe('Component Tests', () => {
  describe('Signature Management Component', () => {
    let comp: SignatureComponent;
    let fixture: ComponentFixture<SignatureComponent>;
    let service: SignatureService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [SignatureComponent],
      })
        .overrideTemplate(SignatureComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SignatureComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(SignatureService);

      const headers = new HttpHeaders().append('link', 'link;link');
      jest.spyOn(service, 'query').mockReturnValue(
        of(
          new HttpResponse({
            body: [{ id: 123 }],
            headers,
          })
        )
      );
    });

    it('Should call load all on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.signatures?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
